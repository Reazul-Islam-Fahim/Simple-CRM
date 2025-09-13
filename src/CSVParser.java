import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVParser {
    public static List<Map<String, String>> parseCSV(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                // IMPROVED: Enhanced regex to handle quoted fields with commas and preserve empties
                // Handles: "field, with comma", unquoted fields, trailing empties
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (firstLine) {
                    headers = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        headers[i] = values[i].trim().replaceAll("\"", "");
                    }
                    firstLine = false;
                    continue;
                }
                if (values.length == 0) continue;  // Skip empty lines
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String val = (i < values.length ? values[i].trim().replaceAll("\"", "") : "");
                    row.put(headers[i], val);  // Empties become ""
                }
                data.add(row);
            }
        }
        // DEBUG: Print parsed count (remove after testing)
        System.out.println("Parsed " + data.size() + " rows from " + filePath);
        return data;
    }
}