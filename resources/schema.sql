CREATE TABLE IF NOT EXISTS houses (
	id			 INT			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name		 VARCHAR(50)	NOT NULL,
	image_name	 VARCHAR(255),
	description	 VARCHAR(255)	NOT NULL,
	price_max	 INT			NOT NULL,
	price_min	 INT			NOT NULL,
	start_time	 INT			NOT NULL,
	end_time	 INT			NOT NULL,
	holiday      INT            NOT NULL,
	postal_code  VARCHAR(50)	NOT NULL,
	address 	 VARCHAR(255)	NOT NULL,
	phone_number VARCHAR(50)	NOT NULL,
	category_id  BIGINT,
	created_at	 DATETIME		NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at	 DATETIME		NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    furigana VARCHAR(50) NOT NULL,
    postal_code VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,    
    role_id INT NOT NULL, 
    enabled BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,    
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS verification_tokens (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL,        
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) 
);

CREATE TABLE IF NOT EXISTS reservations (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    house_id INT NOT NULL,
    user_id INT NOT NULL,
    reservation_date DATE NOT NULL,
    reservation_time TIME NOT NULL,
    number_of_people INT NOT NULL,
    amount INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (house_id) REFERENCES houses (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    house_id INT NOT NULL,
    user_id INT NOT NULL,   
    score INT NOT NULL, 
    comment VARCHAR(255) NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 
);

CREATE TABLE IF NOT EXISTS favorites (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	house_id INT NOT NULL,
	user_id INT NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (house_id) REFERENCES houses (id),
	FOREIGN KEY (user_id) REFERENCES users (id)
);

--UPDATE users
--SET membership_type = 'PAID'
--WHERE id = 1;

CREATE TABLE IF NOT EXISTS password_reset_token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);




CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


--ALTER TABLE houses ADD COLUMN category_id BIGINT;

--ALTER TABLE houses
--ADD CONSTRAINT fk_houses_category
--FOREIGN KEY (category_id) REFERENCES categories(id);

--INSERT INTO categories (name)
--SELECT DISTINCT category FROM houses;

--UPDATE houses h
--OIN categories c ON h.category = c.name
--SET h.category_id = c.id;

--ALTER TABLE houses ADD COLUMN category_backup VARCHAR(50);

--UPDATE houses SET category_backup = category;

--SHOW COLUMNS FROM houses;

--ALTER TABLE houses DROP COLUMN category;


--DROP TABLE IF EXISTS houses;



-- ALTER TABLE reservations ADD COLUMN canceled BOOLEAN DEFAULT FALSE;



-- ALTER TABLE users ADD COLUMN stripe_customer_id VARCHAR(255);


-- ALTER TABLE users
-- ADD COLUMN membership_type VARCHAR(20) DEFAULT 'FREE' AFTER password; 