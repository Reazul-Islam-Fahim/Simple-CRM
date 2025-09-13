import java.util.List;
import java.util.Map;

public class DataTransformer {
    public static void transformData(List<Map<String, String>> data, String tableName) {
        if ("accounts".equals(tableName)) {
            for (Map<String, String> row : data) {
                // Clean missing values
                if (!row.containsKey("subsidiary_of") || row.get("subsidiary_of").isEmpty()) {
                    row.put("subsidiary_of", "None");
                }
                // Transform sector to title case and fix typos
                String sector = row.get("sector");
                if (sector != null && !sector.isEmpty()) {
                    sector = sector.toLowerCase();
                    if ("technolgy".equals(sector)) {
                        sector = "Technology";
                    } else {
                        sector = sector.substring(0, 1).toUpperCase() + sector.substring(1);
                    }
                    row.put("sector", sector);
                }
            }
        }
        // For other tables, handle if needed (e.g., parse dates, but they are strings)
    }
}