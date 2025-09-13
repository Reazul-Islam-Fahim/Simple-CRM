import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MySQLLoader {

    private static final String URL = "jdbc:mysql://localhost:3306/crm_warehouse?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        // NEW: Explicitly load the driver for Connector/J 9.4.0 (prevents "No suitable driver" errors)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage(), e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void loadData(List<Map<String, String>> data, String tableName) throws SQLException {
        try (Connection conn = getConnection()) {
            String insertQuery = getInsertQuery(tableName);
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                for (Map<String, String> row : data) {
                    setParameters(pstmt, row, tableName);
                    pstmt.executeUpdate();
                }
            }
        }
    }

    private static String getInsertQuery(String tableName) {
        switch (tableName) {
            case "accounts":
                return "INSERT IGNORE INTO accounts (account, sector, year_established, revenue, employees, office_location, subsidiary_of) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE sector=VALUES(sector)";
            case "products":
                return "INSERT IGNORE INTO products (product, series, sales_price) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE series=VALUES(series)";
            case "sales_teams":
                return "INSERT IGNORE INTO sales_teams (sales_agent, manager, regional_office) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE manager=VALUES(manager)";
            case "sales_pipeline":
                return "INSERT IGNORE INTO sales_pipeline (opportunity_id, sales_agent, product, account, deal_stage, engage_date, close_date, close_value) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE deal_stage=VALUES(deal_stage)";
            default:
                throw new IllegalArgumentException("Unknown table: " + tableName);
        }
    }

    // FIXED: Removed extra closing braces; method is now clean
    private static void setParameters(PreparedStatement pstmt, Map<String, String> row, String tableName) throws SQLException {
        int paramIndex = 1;
        switch (tableName) {
            case "accounts":
                pstmt.setString(paramIndex++, row.getOrDefault("account", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("sector", ""));
                pstmt.setInt(paramIndex++, parseInt(row.getOrDefault("year_established", "0")));
                pstmt.setDouble(paramIndex++, parseDouble(row.getOrDefault("revenue", "0")));
                pstmt.setInt(paramIndex++, parseInt(row.getOrDefault("employees", "0")));
                pstmt.setString(paramIndex++, row.getOrDefault("office_location", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("subsidiary_of", "None"));
                break;
            case "products":
                pstmt.setString(paramIndex++, row.getOrDefault("product", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("series", ""));
                pstmt.setInt(paramIndex++, parseInt(row.getOrDefault("sales_price", "0")));
                break;
            case "sales_teams":
                pstmt.setString(paramIndex++, row.getOrDefault("sales_agent", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("manager", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("regional_office", ""));
                break;
            case "sales_pipeline":
                pstmt.setString(paramIndex++, row.getOrDefault("opportunity_id", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("sales_agent", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("product", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("account", ""));
                pstmt.setString(paramIndex++, row.getOrDefault("deal_stage", ""));
                String engageDate = row.getOrDefault("engage_date", "");
                pstmt.setString(paramIndex++, engageDate.isEmpty() ? null : engageDate);
                String closeDate = row.getOrDefault("close_date", "");
                pstmt.setString(paramIndex++, closeDate.isEmpty() ? null : closeDate);
                pstmt.setInt(paramIndex++, parseInt(row.getOrDefault("close_value", "0")));
                break;
        }
    }

    private static int parseInt(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDouble(String s) {
        if (s == null || s.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}