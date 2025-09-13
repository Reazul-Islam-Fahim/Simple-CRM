
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ETL_Main {

    public static void main(String[] args) {
        try {
            // Parse and load products from XML
            List<Map<String, String>> products = XMLParser.parseXML("..\\data\\products.xml", "products");
            DataTransformer.transformData(products, "products");
            MySQLLoader.loadData(products, "products");

            // Parse and load accounts from XML
            List<Map<String, String>> accounts = XMLParser.parseXML("..\\data\\accounts.xml", "accounts");
            DataTransformer.transformData(accounts, "accounts");
            MySQLLoader.loadData(accounts, "accounts");

            // Parse and load sales_teams from CSV
            List<Map<String, String>> salesTeams = CSVParser.parseCSV("..\\data\\sales_teams.csv");
            DataTransformer.transformData(salesTeams, "sales_teams");
            MySQLLoader.loadData(salesTeams, "sales_teams");

            // Parse and load sales_pipeline from CSV
            List<Map<String, String>> pipeline = CSVParser.parseCSV("..\\data\\sales_pipeline.csv");
            DataTransformer.transformData(pipeline, "sales_pipeline");
            MySQLLoader.loadData(pipeline, "sales_pipeline");

            System.out.println("ETL process completed successfully.");
        } catch (IOException | SQLException e) {
            System.err.println("Error during ETL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
