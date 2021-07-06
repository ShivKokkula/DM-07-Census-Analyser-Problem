package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndianStateCodeCSV {

    @CsvBindByName(column = "State Name", required = true)
    public String state;

    @CsvBindByName(column = "StateCode", required = true)
    public String StateCode;

    @Override
    public String toString() {
        return "IndianStateCodeCSV{" +
                "state='" + state + '\'' +
                ", StateCode='" + StateCode + '\'' +
                '}';
    }
}
