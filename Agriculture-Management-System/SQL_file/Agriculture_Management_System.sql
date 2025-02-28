create database Agriculture_Management_System;
use Agriculture_Management_System;

-- Creating Landforms Table
-- Creating Crops Table
CREATE TABLE Crops (
    crop_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_name VARCHAR(100) UNIQUE NOT NULL
    
);

INSERT INTO Crops (crop_name) VALUES 
('Rice'), ('Sugarcane'), ('Jute'), ('Banana'),  
('Tea'), ('Coffee'), ('Apple'), ('Walnut'),  
('Sorghum'), ('Cotton'), ('Finger Millet'), ('Groundnut'),  
('Wheat'), ('Maize'), ('Sunflower'), ('Barley'),  
('Coconut'), ('Cashew'), ('Salt-Tolerant Rice'),  
('Date Palm'), ('Guar'), ('Pearl Millet'), ('Aloe Vera');


-- Creating CropPeriods Table
CREATE TABLE CropPeriods (
    period_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_id INT NOT NULL,  -- Removed AUTO_INCREMENT
    total_period INT NOT NULL,  -- Total crop duration in days
    growth_period INT NOT NULL,  -- Vegetative growth stage in days
    productivity_period INT NOT NULL,  -- Harvest or yield stage in days
    FOREIGN KEY (crop_id) REFERENCES Crops(crop_id) ON DELETE CASCADE
);

-- Fixed INSERT statement: Added `crop_id`
INSERT INTO CropPeriods (crop_id, total_period, growth_period, productivity_period) VALUES
(1, 150, 90, 60),   -- Rice
(2, 365, 240, 125), -- Sugarcane
(3, 120, 80, 40),   -- Jute
(4, 300, 180, 120), -- Banana
(5, 450, 300, 150), -- Tea
(6, 365, 250, 115), -- Coffee
(7, 200, 150, 50),  -- Apple
(8, 240, 180, 60),  -- Walnut
(9, 210, 160, 50),  -- Sorghum
(10, 120, 70, 50),  -- Cotton
(11, 180, 110, 70), -- Finger Millet
(12, 110, 75, 35),  -- Groundnut
(13, 140, 90, 50),  -- Wheat
(14, 180, 120, 60), -- Maize
(15, 130, 90, 40),  -- Sunflower
(16, 150, 100, 50), -- Barley
(17, 120, 85, 35),  -- Coconut
(18, 365, 300, 65), -- Cashew
(19, 200, 140, 60), -- Salt-Tolerant Rice
(20, 140, 90, 50),  -- Date Palm
(21, 365, 250, 115), -- Guar
(22, 90, 60, 30),   -- Pearl Millet
(23, 120, 80, 40);  -- Aloe Vera
SELECT * FROM Crops;

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

CREATE TABLE FertilizerReduction (
    reduction_id INT PRIMARY KEY AUTO_INCREMENT,
    previous_crop_id INT NOT NULL,  -- The nitrogen-fixing crop planted previously
    current_crop_id INT NOT NULL,   -- The crop currently being planted
    nitrogen_reduction DECIMAL(10,2) DEFAULT 0,  -- Reduction in Nitrogen (kg/ha)
    phosphorus_reduction DECIMAL(10,2) DEFAULT 0, -- Reduction in Phosphorus (kg/ha)
    potassium_reduction DECIMAL(10,2) DEFAULT 0,  -- Reduction in Potassium (kg/ha)
    FOREIGN KEY (previous_crop_id) REFERENCES Crops(crop_id) ON DELETE CASCADE,
    FOREIGN KEY (current_crop_id) REFERENCES Crops(crop_id) ON DELETE CASCADE
);
INSERT INTO FertilizerReduction (previous_crop_id, current_crop_id, nitrogen_reduction, phosphorus_reduction, potassium_reduction) VALUES
(12, 1, 20, 5, 0),  -- Groundnut before Rice reduces 20 kg/ha Nitrogen and 5 kg/ha Phosphorus
(21, 2, 25, 8, 0),  -- Guar before Sugarcane reduces 25 kg/ha Nitrogen and 8 kg/ha Phosphorus
(12, 14, 15, 4, 0), -- Groundnut before Maize reduces 15 kg/ha Nitrogen and 4 kg/ha Phosphorus
(21, 10, 18, 6, 0), -- Guar before Cotton reduces 18 kg/ha Nitrogen and 6 kg/ha Phosphorus
(22, 16, 10, 3, 0); -- Pearl Millet before Barley reduces 10 kg/ha Nitrogen and 3 kg/ha Phosphorus
