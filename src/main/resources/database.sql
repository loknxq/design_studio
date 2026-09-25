CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS designers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS design_orders (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(12, 2) NOT NULL CHECK (price > 0),
    status VARCHAR(30) NOT NULL,
    created_date DATE NOT NULL,
    client_id INT NOT NULL,
    designer_id INT NOT NULL,
    CONSTRAINT fk_order_client FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_order_designer FOREIGN KEY (designer_id) REFERENCES designers(id),
    CONSTRAINT chk_status CHECK (status IN ('NEW', 'IN_PROGRESS', 'DONE', 'CANCELLED'))
);