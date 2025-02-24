CREATE DATABASE CropFertilizerDatabase;
USE CropFertilizerDatabase;

show tables;

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


CREATE TABLE SoilTypes (
    soil_id INT PRIMARY KEY AUTO_INCREMENT,
    soil_name VARCHAR(100) UNIQUE NOT NULL
);
INSERT INTO SoilTypes (soil_name) VALUES 
('Alluvial Soil'), ('Loamy Soil'), ('Volcanic Soil'), 
('Black Soil'), ('Laterite Soil'), ('Clayey Soil'), 
('Sandy Soil'), ('Saline Soil'), ('Arid Soil');


CREATE TABLE Crops (
    crop_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_name VARCHAR(100) UNIQUE NOT NULL,
    landform_id INT,
    soil_id INT,
    FOREIGN KEY (landform_id) REFERENCES Landforms(landform_id) ON DELETE CASCADE,
    FOREIGN KEY (soil_id) REFERENCES SoilTypes(soil_id) ON DELETE CASCADE
);
INSERT INTO Crops (crop_name, landform_id, soil_id) VALUES 
('Rice', 1, 1), 
('Sugarcane', 1, 1), 
('Jute', 1, 1), 
('Banana', 1, 1),  
('Tea', 2, 2),  
('Coffee', 2, 2),  
('Apple', 2, 2),  
('Walnut', 2, 2),  
('Sorghum', 3, 4),  
('Cotton', 3, 4),  
('Finger Millet', 3, 4),  
('Groundnut', 3, 4),  
('Wheat', 4, 5),  
('Maize', 4, 5),  
('Sunflower', 4, 5),  
('Barley', 4, 5),  
('Coconut', 5, 7),  
('Cashew', 5, 7),  
('Salt-Tolerant Rice', 5, 8),  
('Date Palm', 6, 9),  
('Guar', 6, 9),  
('Pearl Millet', 6, 9),  
('Aloe Vera', 6, 9);


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
(1, 'natural', 3000, NULL, NULL, NULL),  -- Rice (natural, organic matter)
(2, 'man-made', NULL, 100, 50, 60),  -- Sugarcane (man-made)
(3, 'man-made', NULL, 90, 40, 50),  -- Jute (man-made)
(4, 'man-made', NULL, 110, 60, 70),  -- Banana (man-made)
(5, 'natural', 2500, NULL, NULL, NULL),  -- Tea (natural, organic matter)
(6, 'man-made', NULL, 120, 70, 80),  -- Coffee (man-made)
(7, 'natural', 2200, NULL, NULL, NULL),  -- Apple (natural, organic matter)
(8, 'man-made', NULL, 150, 90, 100),  -- Walnut (man-made)
(9, 'man-made', NULL, 80, 60, 70),  -- Sorghum (man-made)
(10, 'man-made', NULL, 100, 50, 60),  -- Cotton (man-made)
(11, 'natural', 2700, NULL, NULL, NULL),  -- Finger Millet (natural, organic matter)
(12, 'man-made', NULL, 90, 45, 55),  -- Groundnut (man-made)
(13, 'man-made', NULL, 150, 75, 85),  -- Wheat (man-made)
(14, 'man-made', NULL, 130, 70, 80),  -- Maize (man-made)
(15, 'natural', 2000, NULL, NULL, NULL),  -- Sunflower (natural, organic matter)
(16, 'man-made', NULL, 110, 50, 60),  -- Barley (man-made)
(17, 'natural', 3000, NULL, NULL, NULL),  -- Coconut (natural, organic matter)
(18, 'man-made', NULL, 90, 50, 60),  -- Cashew (man-made)
(19, 'natural', 2500, NULL, NULL, NULL),  -- Salt-Tolerant Rice (natural, organic matter)
(20, 'man-made', NULL, 120, 70, 80),  -- Date Palm (man-made)
(21, 'man-made', NULL, 70, 40, 50),  -- Guar (man-made)
(22, 'man-made', NULL, 80, 60, 70),  -- Pearl Millet (man-made)
(23, 'man-made', NULL, 90, 65, 75);  -- Aloe Vera (man-made)

select * from soiltypes;