import generator.SimulateData;
import importer.ImportData;

import java.util.function.Function;

public class ImportDataApplication {
    public static void main(String[] args) {
        /*
        * Simulate BUS data file
        * */
        int total= 100000;
//        int total= 3;

        Function<Integer, Object> simulateData=new SimulateData();
        simulateData.apply(total);

        /*
         * Import BUS data to DataCenter
         * */
        Function<String, String> importData=new ImportData();
        importData.apply("data/import/import.txt");
    }
}
