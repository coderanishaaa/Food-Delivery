CREATE DATABASE IF NOT EXISTS user_db;
CREATE DATABASE IF NOT EXISTS restaurant_db;
CREATE DATABASE IF NOT EXISTS order_db;
CREATE DATABASE IF NOT EXISTS payment_db;
CREATE DATABASE IF NOT EXISTS delivery_db;

CREATE USER IF NOT EXISTS 'food_user'@'%' IDENTIFIED BY 'rootpassword';
GRANT ALL PRIVILEGES ON user_db.* TO 'food_user'@'%';
GRANT ALL PRIVILEGES ON restaurant_db.* TO 'food_user'@'%';
GRANT ALL PRIVILEGES ON order_db.* TO 'food_user'@'%';
GRANT ALL PRIVILEGES ON payment_db.* TO 'food_user'@'%';
GRANT ALL PRIVILEGES ON delivery_db.* TO 'food_user'@'%';
FLUSH PRIVILEGES;
