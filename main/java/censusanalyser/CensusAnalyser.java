package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<CensusDAO> censusList = null;
    Map<String, CensusDAO> censusStateMap = null;

    public CensusAnalyser() {

    }

    public int loadStateCensusData(String csvFilePath) throws CensusAnalyserException {
        censusStateMap = new CensusLoader().loadCensusData(csvFilePath,StateCensusCSV.class);
        return censusStateMap.size();
    }

    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianStateCodeCSV> stateCSVIterator = csvBuilder.getCSVFileIterator(reader,IndianStateCodeCSV.class);
            Iterable<IndianStateCodeCSV> csvIterable = () -> stateCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(csvState -> censusStateMap.get(csvState.state) != null)
                    .forEach(csvState -> censusStateMap.get(csvState.state).stateCode = csvState.StateCode);
            return censusStateMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        censusStateMap = new CensusLoader().loadCensusData(csvFilePath,USCensusCSV.class);
        return censusStateMap.size();
    }



    private<E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(),false).
                                                count();
        return numOfEnteries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() ==0){
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.state);
        this.sort(censusCSVComparator);
        String sortedStateCensusJson = new Gson().toJson(this.censusList);
        return sortedStateCensusJson;
    }

    private void sort(Comparator<CensusDAO> censusCSVComparator) {
        for (int i=0; i < censusList.size()-1; i++){
            for (int j=0; j < censusList.size()-i-1; j++){
                CensusDAO census1 = censusList.get(j);
                CensusDAO census2 = censusList.get(j+1);
                if (censusCSVComparator.compare(census1,census2) > 0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }
    }
}
