DROP TABLE IF EXISTS design_orders;
DROP TABLE IF EXISTS designers;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL
);

CREATE TABLE designers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE design_orders (
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

INSERT INTO clients (full_name, email, phone) VALUES
('Иванов Иван Иванович', 'ivanov@mail.ru', '+79001112233'),
('Петрова Анна Сергеевна', 'petrova@mail.ru', '+79002223344'),
('Сидоров Пётр Алексеевич', 'sidorov@mail.ru', '+79003334455'),
('Кузнецова Мария Ивановна', 'kuznetsova@mail.ru', '+79004445566'),
('Смирнов Алексей Петрович', 'smirnov@mail.ru', '+79005556677');

INSERT INTO designers (full_name, specialization, email) VALUES
('Орлова Екатерина Дмитриевна', 'Графический дизайн', 'orlova@studio.ru'),
('Морозов Дмитрий Сергеевич', 'Веб-дизайн', 'morozov@studio.ru'),
('Волкова Ольга Андреевна', 'Интерьерный дизайн', 'volkova@studio.ru');

INSERT INTO design_orders (title, description, price, status, created_date, client_id, designer_id) VALUES
('Логотип для кофейни', 'Разработка фирменного логотипа', 25000.00, 'NEW', '2024-01-10', 1, 1),
('Сайт для магазина', 'Дизайн главной страницы интернет-магазина', 80000.00, 'IN_PROGRESS', '2024-01-15', 2, 2),
('Дизайн квартиры', 'Проект дизайна гостиной 30 кв.м.', 120000.00, 'IN_PROGRESS', '2024-01-20', 3, 3),
('Визитки для клиники', 'Печать визиток сотрудников', 15000.00, 'DONE', '2024-02-01', 4, 1),
('Баннер для рекламы', 'Рекламный баннер 3x6 метров', 20000.00, 'DONE', '2024-02-05', 5, 1),
('Дизайн кухни', 'Проект кухни 12 кв.м.', 90000.00, 'CANCELLED', '2024-02-10', 1, 3),
('Фирменный стиль', 'Разработка брендбука компании', 150000.00, 'NEW', '2024-02-12', 2, 1),
('Лендинг для курсов', 'Одностраничный сайт', 45000.00, 'IN_PROGRESS', '2024-02-15', 3, 2),
('Дизайн спальни', 'Проект спальни 18 кв.м.', 95000.00, 'NEW', '2024-02-18', 4, 3),
('Упаковка товара', 'Дизайн упаковки для чая', 35000.00, 'DONE', '2024-02-20', 5, 1),
('Дизайн офиса', 'Проект офиса 100 кв.м.', 200000.00, 'IN_PROGRESS', '2024-02-22', 1, 3),
('Обложка журнала', 'Дизайн обложки ежемесячного журнала', 30000.00, 'NEW', '2024-02-25', 2, 1);