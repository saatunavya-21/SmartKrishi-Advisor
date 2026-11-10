-- =====================================================================
-- SMART KRISHI MARKET ADVISOR - DATABASE SCHEMA SETUP SCRIPT
-- Compatible with MySQL 8.0+
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `smart_krishi_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `smart_krishi_db`;

-- 1. Farmers Table
CREATE TABLE IF NOT EXISTS `farmers` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(255),
    `phone` VARCHAR(20),
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `village` VARCHAR(255),
    `district` VARCHAR(255),
    `state` VARCHAR(255),
    `land_area` DOUBLE NOT NULL DEFAULT 0.0,
    `soil_type` VARCHAR(100),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Buyers Table
CREATE TABLE IF NOT EXISTS `buyers` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20),
    `village` VARCHAR(255),
    `district` VARCHAR(255),
    `state` VARCHAR(255),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Produce Table
CREATE TABLE IF NOT EXISTS `produce` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `farmer_id` BIGINT NOT NULL,
    `crop_name` VARCHAR(255) NOT NULL,
    `quantity` DOUBLE NOT NULL,
    `unit` VARCHAR(50) NOT NULL,
    `expected_price` DOUBLE NOT NULL,
    `quality` VARCHAR(100),
    `location` VARCHAR(255),
    PRIMARY KEY (`id`),
    INDEX `idx_farmer_id` (`farmer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Crop Demand Table
CREATE TABLE IF NOT EXISTS `crop_demand` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `buyer_id` BIGINT NOT NULL,
    `crop_name` VARCHAR(255) NOT NULL,
    `quantity` DOUBLE NOT NULL,
    `unit` VARCHAR(50) NOT NULL,
    `location` VARCHAR(255),
    PRIMARY KEY (`id`),
    INDEX `idx_buyer_id` (`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Market Benchmark Prices Table
CREATE TABLE IF NOT EXISTS `market_prices` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `crop_name` VARCHAR(255) NOT NULL,
    `market_name` VARCHAR(255) NOT NULL,
    `district` VARCHAR(255) NOT NULL,
    `price` DOUBLE NOT NULL,
    `unit` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_crop_name` (`crop_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Feedback Table
CREATE TABLE IF NOT EXISTS `feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `farmer_id` BIGINT,
    `category` VARCHAR(255),
    `rating` INT NOT NULL,
    `comments` VARCHAR(1000),
    `submitted_at` DATETIME(6),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Initial Benchmark Market Price Seed Data
INSERT IGNORE INTO `market_prices` (`id`, `crop_name`, `market_name`, `district`, `price`, `unit`) VALUES
(1, 'Wheat', 'Central Mandi', 'Karnal', 2450.0, 'Quintal'),
(2, 'Paddy (Basmati)', 'City Grain Market', 'Amritsar', 3850.0, 'Quintal'),
(3, 'Cotton', 'Cotton Market Yard', 'Warangal', 7150.0, 'Quintal'),
(4, 'Soybean', 'Agri Mandi', 'Indore', 4620.0, 'Quintal'),
(5, 'Tomato', 'APMC Yard', 'Nashik', 2100.0, 'Quintal'),
(6, 'Maize', 'District Mandi', 'Nagpur', 2180.0, 'Quintal');
