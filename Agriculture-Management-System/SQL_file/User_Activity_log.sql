-- Create activity_log table if not already done
CREATE TABLE activity_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    module_name VARCHAR(50) NOT NULL, -- home, crop-management, fertilizer-management, about-us
    access_time DATETIME NOT NULL,
    access_count INT DEFAULT 1
);

-- Example data for all four modules
INSERT INTO activity_log (username, module_name, access_time, access_count)
VALUES
    ('admin', 'home', NOW(), 1),
    ('admin', 'crop-management', NOW(), 1),
    ('admin', 'fertilizer-management', NOW(), 1),
    ('user1', 'about-us', NOW(), 2);  -- User 1 accessed about-us twice
show tables;
create database login;
use login;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);
select * from users;



show tables;