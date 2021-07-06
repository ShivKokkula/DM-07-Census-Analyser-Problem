package censusanalyser;

public class CensusDAO {
    public int population;
    public int densityPerSqKm;
    public int areaInSqKm;
    public String state;
    public String stateCode;

    public CensusDAO(StateCensusCSV indiaCensusDAO) {
        state = indiaCensusDAO.state;
        areaInSqKm = indiaCensusDAO.areaInSqKm;
        densityPerSqKm = indiaCensusDAO.densityPerSqKm;
        population = indiaCensusDAO.population;
    }

    public CensusDAO(USCensusCSV censusCSV) {
        state = censusCSV.state;
        areaInSqKm = censusCSV.areaInSqKm;
        densityPerSqKm = censusCSV.densityPerSqKm;
        population = censusCSV.population;
    }
}
