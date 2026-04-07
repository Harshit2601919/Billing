-- MySQL Data Definition Language (DDL) Script
-- Database Name: billing_db

CREATE DATABASE IF NOT EXISTS billing_db;
USE billing_db;

-- 1. Users Table (Authentication)
CREATE TABLE IF NOT EXISTS users (
    id CHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Partners Table
-- Simplified: name, location (as requested)
CREATE TABLE IF NOT EXISTS partners (
    id CHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bills Table
CREATE TABLE IF NOT EXISTS bills (
    id CHAR(36) NOT NULL PRIMARY KEY,
    bill_number VARCHAR(20) NOT NULL UNIQUE,
    partner_id CHAR(36) NOT NULL,
    bill_date DATE NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (partner_id) REFERENCES partners(id) ON DELETE CASCADE
);

-- 4. Bill Items Table
CREATE TABLE IF NOT EXISTS bill_items (
    id CHAR(36) NOT NULL PRIMARY KEY,
    bill_id CHAR(36) NOT NULL,
    description TEXT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(15, 2) NOT NULL,
    item_total DECIMAL(15, 2) AS (quantity * unit_price) VIRTUAL,
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE
);
