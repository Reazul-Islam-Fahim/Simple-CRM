# CRM Data Warehouse Project

## Overview

This project builds a Customer Relationship Management (CRM) Data Warehouse to extract, transform, and load (ETL) data from XML and CSV files into a MySQL (MariaDB) database. It includes a web-based interface for generating reports and analyses, accessible via a browser. The system processes sales data (products, accounts, sales teams, opportunities) and displays results in styled HTML tables.

### Key Components:

- **ETL Process**: Java scripts extract data from XML (`products.xml`, `accounts.xml`) and CSV (`sales_teams.csv`, `sales_pipeline.csv`), apply transformations (e.g., sector title case, handling missing values), and load into MySQL.
- **Database**: MySQL (MariaDB 10.4.32) with `crm_warehouse` schema (tables: `products`, `accounts`, `sales_teams`, `sales_pipeline`).
- **Web Interface**: PHP-based reports hosted on Apache (XAMPP) at `http://localhost/crm/index.html`, offering four reports/analyses.
- **Documentation**: LaTeX report (`CRM_Data_Warehouse_Report.tex`) summarizing architecture, transformations, and results.

## Prerequisites

- **Windows OS** (tested on Windows 10/11)
- **XAMPP**: For Apache and MySQL (MariaDB 10.4.32). Download from [https://www.apachefriends.org](https://www.apachefriends.org).
- **Java Development Kit (JDK)**: Version 8+ for ETL scripts. Download from [https://www.oracle.com/java](https://www.oracle.com/java).
- **LaTeX Environment**: Overleaf (online) or local pdflatex (e.g., MiKTeX) for report compilation.

## Project Structure
* Assisgnment CRM/
* ├── src/ # Java source files for ETL
* │ ├── CSVParser.java # Parses CSV files (sales_teams.csv, sales_pipeline.csv)
* │ ├── DataTransformer.java # Applies transformations (e.g., sector title case)
* │ ├── ETL_Main.java # Orchestrates ETL process
* │ ├── MySQLLoader.java # Loads data into MySQL via JDBC
* │ └── XMLParser.java # Parses XML files (products.xml, accounts.xml)
* ├── libs/ # External libraries
* │ └── mysql-connector-j-9.4.0.jar # MySQL JDBC driver
* ├── data/ # Input dataset files
* │ ├── accounts.xml # ~90 account records
* │ ├── products.xml # 7 product records
* │ ├── sales_teams.csv # 36 sales team records
* │ └── sales_pipeline.csv # ~1000+ sales opportunity records
* ├── warehouse.sql # MySQL schema and data export
* └── CRM_Data_Warehouse_Report.tex # LaTeX report with team contributions, screenshots

* xampp/ # XAMPP installation (default: D:\xampp)
* └── htdocs/
* └── crm/ # Web interface files
* ├── index.html # Homepage with report links
* └── queryEngine.php # PHP script for queries and reports


## Setup Instructions

### 1. Install XAMPP

1. Install XAMPP to `D:\xampp` (default path).
2. Open XAMPP Control Panel (`D:\xampp\xampp-control.exe`).
3. Start Apache and MySQL (ensure green status).
4. If MySQL fails: Check `D:\xampp\mysql\data\mysql_error.log` for issues (e.g., port 3306 conflict).

### 2. Set Up MySQL Database

1. Open phpMyAdmin: `http://localhost/phpmyadmin`.
2. Login: Username `root`, Password blank (default).
3. Create database:
   - Click "New" > Enter `crm_warehouse` > Create.
4. Import schema:
   - Select `crm_warehouse` > "Import" tab > Choose `warehouse.sql` > Go.
   - Creates tables: `products`, `accounts`, `sales_teams`, `sales_pipeline`.

### 3. Place Project Files

1. Extract project to `C:\Users\[YourUsername]\Downloads\Assisgnment CRM`.
2. Verify folders: `src/`, `libs/`, `data/`, and `CRM_Data_Warehouse_Report.tex`.
3. Copy web files:
   - Create `D:\xampp\htdocs\crm`.
   - Copy `index.html` and `queryEngine.php` to `D:\xampp\htdocs\crm`.

### 4. Run ETL Process

1. Open Command Prompt (or MinGW64).
2. Navigate to `src/`:
   ```bash
   cd C:\Users\[YourUsername]\Downloads\Assisgnment CRM\src
   ```
3. Compile Java files:
   ```bash
   javac -cp ".;..\libs\mysql-connector-j-9.4.0.jar" *.java
   ```
* Expected: Silent success (creates .class files)
4. Run ETL:
   ```bash
   java -cp ".;..\libs\mysql-connector-j-9.4.0.jar" ETL_Main
   ```
Expected output:
   ```bash
   Parsed 7 records from ..\data\products.xml for table products
   Parsed 90 records from ..\data\accounts.xml for table accounts
   Parsed 36 rows from ..\data\sales_teams.csv
   Parsed [1000+] rows from ..\data\sales_pipeline.csv
   ETL process completed successfully.
   ```

* If errors: Check data/ file paths, MySQL connection (root, no password), or share output


### 5. Test Web Interface
1. Ensure Apache and MySQL are running in XAMPP
2. Open browser: `http://localhost/crm/index.html`
3. Expected: Homepage with "CRM Reports and Analyses" and four links:
   - Products Report: 7 rows (e.g., "GTX Basic", $550)
   - Sales Opportunities Report: ~500 rows (Won/Lost deals)
   - Establishment Year Revenue Analysis: Revenue by year (e.g., 1980: $7708.38M)
   - Sales Opportunity Analysis: Total value by produ
4.If "No data": Re-run ETL or verify tables in phpMyAdmin (`SELECT COUNT(*) FROM products;`)