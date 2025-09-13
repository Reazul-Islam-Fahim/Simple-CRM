<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "crm_warehouse";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$report = $_GET['report'] ?? '';

echo "<!DOCTYPE html><html><head><title>CRM Report</title><style>table {border-collapse: collapse; width: 100%;} th, td {border: 1px solid #ddd; padding: 8px; text-align: left;} th {background-color: #f2f2f2;}</style></head><body>";
echo "<h1>" . htmlspecialchars(ucwords(str_replace('_', ' ', $report))) . "</h1>";

if ($report === 'products_report') {
    $sql = "SELECT * FROM products";
    $result = $conn->query($sql);
    if ($result && $result->num_rows > 0) {
        echo "<table><tr><th>Product</th><th>Series</th><th>Sales Price</th></tr>";
        while($row = $result->fetch_assoc()) {
            echo "<tr><td>" . htmlspecialchars($row["product"]) . "</td><td>" . htmlspecialchars($row["series"]) . "</td><td>$" . htmlspecialchars($row["sales_price"]) . "</td></tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No data available.</p>";
    }
} elseif ($report === 'sales_opportunities_report') {
    $sql = "SELECT sp.opportunity_id, sp.close_value, p.product FROM sales_pipeline sp JOIN products p ON sp.product = p.product WHERE sp.deal_stage IN ('Won', 'Lost')";
    $result = $conn->query($sql);
    if ($result && $result->num_rows > 0) {
        echo "<table><tr><th>Opportunity ID</th><th>Close Value</th><th>Product</th></tr>";
        while($row = $result->fetch_assoc()) {
            echo "<tr><td>" . htmlspecialchars($row["opportunity_id"]) . "</td><td>$" . htmlspecialchars($row["close_value"]) . "</td><td>" . htmlspecialchars($row["product"]) . "</td></tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No data available.</p>";
    }
} elseif ($report === 'establishment_year_revenue_analysis') {
    $sql = "SELECT year_established, SUM(revenue) AS total_revenue FROM accounts GROUP BY year_established ORDER BY year_established";
    $result = $conn->query($sql);
    if ($result && $result->num_rows > 0) {
        echo "<table><tr><th>Year Established</th><th>Total Revenue (Millions USD)</th></tr>";
        while($row = $result->fetch_assoc()) {
            echo "<tr><td>" . htmlspecialchars($row["year_established"]) . "</td><td>$" . number_format($row["total_revenue"], 2) . "</td></tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No data available.</p>";
    }
} elseif ($report === 'sales_opportunity_analysis') {
    $sql = "SELECT p.product, SUM(sp.close_value) AS total_value FROM sales_pipeline sp JOIN products p ON sp.product = p.product GROUP BY p.product ORDER BY total_value DESC";
    $result = $conn->query($sql);
    if ($result && $result->num_rows > 0) {
        echo "<table><tr><th>Product</th><th>Total Opportunity Value</th></tr>";
        while($row = $result->fetch_assoc()) {
            echo "<tr><td>" . htmlspecialchars($row["product"]) . "</td><td>$" . number_format($row["total_value"], 2) . "</td></tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No data available.</p>";
    }
} else {
    echo "<p>Invalid report selected.</p>";
}

echo "<br><a href='index.html'>Back to Home</a>";
echo "</body></html>";

$conn->close();
?>