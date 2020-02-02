DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id                    bigserial,
  phone                 VARCHAR(15) NOT NULL UNIQUE,
  password              VARCHAR(80),
  email                 VARCHAR(50) UNIQUE,
  first_name            VARCHAR(50),
  last_name             VARCHAR(50),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  id                    serial,
  name                  VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles (
  user_id               INT NOT NULL,
  role_id               INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id)
  REFERENCES users (id),
  FOREIGN KEY (role_id)
  REFERENCES roles (id)
);

INSERT INTO roles (name)
VALUES
('ROLE_CUSTOMER'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (phone, password, first_name, last_name, email)
VALUES
('11111111','$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i','Admin','Admin','admin@gmail.com');

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3);

DROP TABLE IF EXISTS categories;
CREATE TABLE categories (id bigserial PRIMARY KEY, title varchar(255));
INSERT INTO categories (title) VALUES
('Продукты'), ('Электроника'), ('Химия');

DROP TABLE IF EXISTS products;
CREATE TABLE products (id bigserial PRIMARY KEY, title varchar(255), price numeric(8, 2), category_id bigint, FOREIGN KEY (category_id) REFERENCES categories(id));
INSERT INTO products (title, price, category_id) VALUES
('Cheese', 320.0, 1),
('Milk', 90.0, 1),
('Apples', 120.0, 1),
('iPad', 1000.0, 2),
('Tide', 50.0, 3);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id bigserial PRIMARY KEY,
    user_id bigint,
    price numeric(8, 2),
    phone varchar(15),
    address varchar(255),
    status varchar(255),
    payment_id varchar(255),
    approval_url varchar(255),
    created_at timestamp,
    updated_at timestamp,
    FOREIGN KEY (user_id)
        REFERENCES users (id));

DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (id bigserial PRIMARY KEY, order_id bigint, product_id bigint, quantity int, item_price numeric(8, 2),
total_price numeric(8, 2), FOREIGN KEY (order_id) REFERENCES orders(id), FOREIGN KEY (product_id) REFERENCES products(id));

DROP TABLE IF EXISTS products_images;
CREATE TABLE products_images (id bigserial PRIMARY KEY, product_id bigint, path varchar(255), FOREIGN KEY (product_id) REFERENCES products(id));
INSERT INTO products_images (product_id, path) VALUES
(1, 'img_1.jpg'),
(2, 'img_1.jpg'),
(3, 'img_1.jpg'),
(4, 'img_1.jpg'),
(5, 'img_1.jpg');

DROP TABLE IF EXISTS products_history;
CREATE TABLE products_history (
    id bigserial PRIMARY KEY,
    user_id bigint,
    product_id bigint,
    created_at timestamp,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
                              );