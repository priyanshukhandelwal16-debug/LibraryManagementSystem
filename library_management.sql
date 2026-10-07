-- =========================================================
-- Library Management System - Database Script
-- Database: library_management
-- =========================================================

DROP DATABASE IF EXISTS library_management;
CREATE DATABASE library_management;
USE library_management;

-- ---------------------------------------------------------
-- TABLE: admin
-- ---------------------------------------------------------
CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Default admin login -> username: admin / password: admin123
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- ---------------------------------------------------------
-- TABLE: books
-- ---------------------------------------------------------
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(150) NOT NULL,
    publisher VARCHAR(150),
    category VARCHAR(100),
    publication_year INT,
    quantity INT NOT NULL DEFAULT 0,
    available_quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- TABLE: members
-- ---------------------------------------------------------
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(15),
    gender VARCHAR(10),
    course VARCHAR(100),
    semester VARCHAR(20),
    address VARCHAR(255),
    registration_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- TABLE: transactions
-- ---------------------------------------------------------
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    fine DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trans_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_trans_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- SAMPLE BOOKS
-- ---------------------------------------------------------
INSERT INTO books (isbn, title, author, publisher, category, publication_year, quantity, available_quantity) VALUES
('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 'Programming', 2018, 5, 5),
('978-0596009205', 'Head First Design Patterns', 'Eric Freeman', 'O''Reilly Media', 'Programming', 2004, 4, 4),
('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 'Programming', 2008, 3, 3),
('978-0071392319', 'Database System Concepts', 'Abraham Silberschatz', 'McGraw-Hill', 'Database', 2010, 6, 6),
('978-0262033848', 'Introduction to Algorithms', 'Thomas H. Cormen', 'MIT Press', 'Computer Science', 2009, 4, 4),
('978-0134494166', 'Clean Architecture', 'Robert C. Martin', 'Prentice Hall', 'Programming', 2017, 2, 2),
('978-9332555316', 'Operating System Concepts', 'Abraham Silberschatz', 'Wiley', 'Computer Science', 2014, 3, 3),
('978-0201633610', 'Design Patterns', 'Erich Gamma', 'Addison-Wesley', 'Programming', 1994, 2, 2);

-- ---------------------------------------------------------
-- SAMPLE MEMBERS
-- ---------------------------------------------------------
INSERT INTO members (member_id, full_name, email, phone, gender, course, semester, address, registration_date) VALUES
('MEM1001', 'Aarav Sharma', 'aarav.sharma@example.com', '9876543210', 'Male', 'B.Tech CSE', '5', 'Ahmedabad, Gujarat', '2026-01-10'),
('MEM1002', 'Priya Patel', 'priya.patel@example.com', '9876500001', 'Female', 'BCA', '3', 'Surat, Gujarat', '2026-02-15'),
('MEM1003', 'Rohan Mehta', 'rohan.mehta@example.com', '9876500002', 'Male', 'M.Sc IT', '2', 'Vadodara, Gujarat', '2026-03-05'),
('MEM1004', 'Sneha Joshi', 'sneha.joshi@example.com', '9876500003', 'Female', 'B.Tech IT', '7', 'Rajkot, Gujarat', '2026-01-22');

-- ---------------------------------------------------------
-- SAMPLE TRANSACTIONS (one already issued for testing return/fine)
-- ---------------------------------------------------------
INSERT INTO transactions (member_id, book_id, issue_date, due_date, return_date, fine, status) VALUES
(1, 1, '2026-09-01', '2026-09-08', NULL, 0.00, 'ISSUED');

UPDATE books SET available_quantity = available_quantity - 1 WHERE id = 1;

-- Verify
SELECT * FROM admin;
SELECT * FROM books;
SELECT * FROM members;
SELECT * FROM transactions;
