CREATE DATABASE NativeCropsDB;
USE NativeCropsDB;

-- Creating Crops Table
CREATE TABLE Crops (
    crop_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_name VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO Crops (crop_name) VALUES 
('Rice'), ('Sugarcane'), ('Jute'), ('Banana'), ('Tea'), ('Coffee'), 
('Apple'), ('Walnut'), ('Cardamom'), ('Sorghum'), ('Cotton'), 
('Finger Millet'), ('Groundnut'), ('Wheat'), ('Maize'), 
('Sunflower'), ('Barley'), ('Coconut'), ('Cashew'), 
('Salt-Tolerant Rice'), ('Date Palm'), ('Guar'), 
('Pearl Millet'), ('Aloe Vera');

-- Creating CropPeriods Table
CREATE TABLE CropPeriods (
    period_id INT PRIMARY KEY AUTO_INCREMENT,
    crop_id INT NOT NULL,
    total_period INT NOT NULL,  -- Total crop duration in days
    growth_period INT NOT NULL,  -- Vegetative growth stage in days
    productivity_period INT NOT NULL,  -- Harvest or yield stage in days
    FOREIGN KEY (crop_id) REFERENCES Crops(crop_id) ON DELETE CASCADE
);

-- Inserting Data for Native Crops
INSERT INTO CropPeriods (crop_id, total_period, growth_period, productivity_period) VALUES
(1, 150, 90, 60),   -- Rice (Valley, Bangladesh)
(2, 365, 240, 125), -- Sugarcane (Valley, India)
(3, 120, 80, 40),   -- Jute (Valley, Bangladesh)
(4, 300, 180, 120), -- Banana (Valley, India)
(5, 450, 300, 150), -- Tea (Mountain Slopes, Sri Lanka)
(6, 365, 250, 115), -- Coffee (Mountain Slopes, Ethiopia)
(7, 200, 150, 50),  -- Apple (Mountain Slopes, Nepal)
(8, 240, 180, 60),  -- Walnut (Mountain Slopes, Nepal)
(9, 210, 160, 50),  -- Cardamom (Mountain Slopes, India)
(10, 120, 70, 50),  -- Sorghum (Plateau, Brazil)
(11, 180, 110, 70), -- Cotton (Plateau, India)
(12, 110, 75, 35),  -- Finger Millet (Plateau, India)
(13, 140, 90, 50),  -- Groundnut (Plateau, India)
(14, 180, 120, 60), -- Wheat (Plains, USA)
(15, 130, 90, 40),  -- Maize (Plains, USA)
(16, 150, 100, 50), -- Sunflower (Plains, Ukraine)
(17, 120, 85, 35),  -- Barley (Plains, Russia)
(18, 365, 300, 65), -- Coconut (Coastal, Philippines)
(19, 200, 140, 60), -- Cashew (Coastal, India)
(20, 140, 90, 50),  -- Salt-Tolerant Rice (Coastal, Indonesia)
(21, 365, 250, 115),-- Date Palm (Desert, Saudi Arabia)
(22, 90, 60, 30),   -- Guar (Desert, India)
(23, 120, 80, 40),  -- Pearl Millet (Desert, India)
(24, 240, 180, 60); -- Aloe Vera (Desert, UAE)

-- Verifying Data
SELECT * FROM Crops;
SELECT * FROM CropPeriods;
