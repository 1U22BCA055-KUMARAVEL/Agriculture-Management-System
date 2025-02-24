create database Agriculture_Management_System;
use Agriculture_Management_System;





-- Creating Landforms Table
CREATE TABLE Landforms (
    landform_id INT PRIMARY KEY AUTO_INCREMENT,
    landform_name VARCHAR(100) UNIQUE NOT NULL,
    climate VARCHAR(100) NOT NULL
);

INSERT INTO Landforms (landform_name, climate) VALUES 
('Valley', 'Humid, Fertile'),
('Mountain Slopes', 'Cool, Humid, Temperate'),
('Plateau', 'Semi-Arid, Dry Temperate'),
('Plains', 'Temperate, Subtropical'),
('Coastal', 'Tropical, Humid'),
('Desert', 'Arid, Hot, Dry');

-- Creating SoilTypes Table
CREATE TABLE SoilTypes (
    soil_id INT PRIMARY KEY AUTO_INCREMENT,
    soil_name VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO SoilTypes (soil_name) VALUES 
('Alluvial Soil'), ('Loamy Soil'), ('Volcanic Soil'), 
('Black Soil'), ('Laterite Soil'), ('Clayey Soil'), 
('Sandy Soil'), ('Saline Soil'), ('Arid Soil');

-- Creating Crops Table
CREATE TABLE Crops (
    crop_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_name VARCHAR(100) UNIQUE NOT NULL,
    landform_id INT,
    soil_id INT,
    is_nitrogen_fixer BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (landform_id) REFERENCES Landforms(landform_id) ON DELETE SET NULL,
    FOREIGN KEY (soil_id) REFERENCES SoilTypes(soil_id) ON DELETE SET NULL
);

INSERT INTO Crops (crop_name, landform_id, soil_id) VALUES 
('Rice', 1, 1), ('Sugarcane', 1, 1), ('Jute', 1, 1), ('Banana', 1, 1),  
('Tea', 2, 2), ('Coffee', 2, 2), ('Apple', 2, 2), ('Walnut', 2, 2),  
('Sorghum', 3, 4), ('Cotton', 3, 4), ('Finger Millet', 3, 4), ('Groundnut', 3, 4),  
('Wheat', 4, 5), ('Maize', 4, 5), ('Sunflower', 4, 5), ('Barley', 4, 5),  
('Coconut', 5, 7), ('Cashew', 5, 7), ('Salt-Tolerant Rice', 5, 8),  
('Date Palm', 6, 9), ('Guar', 6, 9), ('Pearl Millet', 6, 9), ('Aloe Vera', 6, 9);

UPDATE Crops SET is_nitrogen_fixer = TRUE WHERE crop_name IN ('Groundnut', 'Soybean', 'Lentils', 'Peas', 'Chickpeas');

-- Creating CropPeriods Table
CREATE TABLE CropPeriods (
    period_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_id INT NOT NULL,
    total_period INT NOT NULL,  -- Total crop duration in days
    growth_period INT NOT NULL,  -- Vegetative growth stage in days
    productivity_period INT NOT NULL,  -- Harvest or yield stage in days
    FOREIGN KEY (crop_id) REFERENCES Crops(crop_id) ON DELETE CASCADE
);

INSERT INTO CropPeriods (crop_id, total_period, growth_period, productivity_period) VALUES
(1, 150, 90, 60), (2, 365, 240, 125), (3, 120, 80, 40), (4, 300, 180, 120), 
(5, 450, 300, 150), (6, 365, 250, 115), (7, 200, 150, 50), (8, 240, 180, 60), 
(9, 210, 160, 50), (10, 120, 70, 50), (11, 180, 110, 70), (12, 110, 75, 35), 
(13, 140, 90, 50), (14, 180, 120, 60), (15, 130, 90, 40), (16, 150, 100, 50), 
(17, 120, 85, 35), (18, 365, 300, 65), (19, 200, 140, 60), (20, 140, 90, 50), 
(21, 365, 250, 115), (22, 90, 60, 30), (23, 120, 80, 40), (24, 240, 180, 60);

-- Creating FertilizerRecommendations Table
CREATE TABLE FertilizerRecommendations (
    fertilizer_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_id INT,
    fertilizer_type ENUM('natural', 'man-made') NOT NULL,
    organic_matter_per_hectare DECIMAL(10,2),  -- Only for natural fertilizer
    nitrogen_kg_per_hectare DECIMAL(10,2),    -- Only for man-made fertilizer
    phosphorus_kg_per_hectare DECIMAL(10,2),  -- Only for man-made fertilizer
    potassium_kg_per_hectare DECIMAL(10,2),   -- Only for man-made fertilizer
    FOREIGN KEY (crop_id) REFERENCES Crops(crop_id) ON DELETE CASCADE
);

INSERT INTO FertilizerRecommendations (crop_id, fertilizer_type, organic_matter_per_hectare, nitrogen_kg_per_hectare, phosphorus_kg_per_hectare, potassium_kg_per_hectare) VALUES 
(1, 'natural', 3000, NULL, NULL, NULL), (2, 'man-made', NULL, 100, 50, 60), 
(3, 'man-made', NULL, 90, 40, 50), (4, 'man-made', NULL, 110, 60, 70), 
(5, 'natural', 2500, NULL, NULL, NULL), (6, 'man-made', NULL, 120, 70, 80), 
(7, 'natural', 2200, NULL, NULL, NULL), (8, 'man-made', NULL, 150, 90, 100), 
(9, 'man-made', NULL, 80, 60, 70), (10, 'man-made', NULL, 100, 50, 60), 
(11, 'natural', 2700, NULL, NULL, NULL), (12, 'man-made', NULL, 90, 45, 55), 
(13, 'man-made', NULL, 150, 75, 85), (14, 'man-made', NULL, 130, 70, 80), 
(15, 'natural', 2000, NULL, NULL, NULL), (16, 'man-made', NULL, 110, 50, 60), 
(17, 'natural', 3000, NULL, NULL, NULL), (18, 'man-made', NULL, 90, 50, 60), 
(19, 'natural', 2500, NULL, NULL, NULL), (20, 'man-made', NULL, 120, 70, 80), 
(21, 'man-made', NULL, 70, 40, 50), (22, 'man-made', NULL, 80, 60, 70), 
(23, 'man-made', NULL, 90, 65, 75);