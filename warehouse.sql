-- Create the database
CREATE DATABASE IF NOT EXISTS crm_warehouse;
USE crm_warehouse;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account VARCHAR(255) PRIMARY KEY,
    sector VARCHAR(255),
    year_established INT,
    revenue DECIMAL(10, 2),
    employees INT,
    office_location VARCHAR(255),
    subsidiary_of VARCHAR(255)
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    product VARCHAR(255) PRIMARY KEY,
    series VARCHAR(255),
    sales_price INT
);

-- Create sales_teams table
CREATE TABLE IF NOT EXISTS sales_teams (
    sales_agent VARCHAR(255) PRIMARY KEY,
    manager VARCHAR(255),
    regional_office VARCHAR(255)
);

-- Create sales_pipeline table
CREATE TABLE IF NOT EXISTS sales_pipeline (
    opportunity_id VARCHAR(255) PRIMARY KEY,
    sales_agent VARCHAR(255),
    product VARCHAR(255),
    account VARCHAR(255),
    deal_stage VARCHAR(255),
    engage_date DATE,
    close_date DATE,
    close_value INT,
    FOREIGN KEY (sales_agent) REFERENCES sales_teams(sales_agent),
    FOREIGN KEY (product) REFERENCES products(product),
    FOREIGN KEY (account) REFERENCES accounts(account)
);

-- Note: Data will be inserted via ETL process