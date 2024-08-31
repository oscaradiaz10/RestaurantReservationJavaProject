DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(20),
    customer_email VARCHAR(255)
);

CREATE TABLE restaurant (
    restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_name VARCHAR(255) NOT NULL,
    restaurant_address VARCHAR(255),
    restaurant_phone VARCHAR(20)
);

CREATE TABLE reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_time TIME NOT NULL,
    reservation_date DATE NOT NULL,
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE customer_restaurant (
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    PRIMARY KEY (customer_id, restaurant_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id)
);
