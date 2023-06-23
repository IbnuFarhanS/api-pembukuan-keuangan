-- Membuat Table tbl_kategori
CREATE TABLE tbl_kategori (
  id INT PRIMARY KEY,
  name VARCHAR(100)
);

-- Membuat Table pengguna
CREATE TABLE pengguna (
  id INT PRIMARY KEY,
  nama_pengguna VARCHAR(100),
  password VARCHAR(100),
  email VARCHAR(100),
  password VARCHAR(100)
);

-- Membuat Table customer
CREATE TABLE customer (
  id INT PRIMARY KEY,
  nama_customer VARCHAR(100),
  nomor VARCHAR (100),
  alamat VARCHAR (100)
);

-- Membuat Table daftar_keuangan
CREATE TABLE daftar_keuangan (
  id INT PRIMARY KEY,
  pengguna_id INT,
  customer_id INT,
  kategori_id INT,
  date DATE,
  amount DECIMAL(10, 2),
  FOREIGN KEY (pengguna_id) REFERENCES pengguna (id),
  FOREIGN KEY (customer_id) REFERENCES customer (id),
  FOREIGN KEY (kategori_id) REFERENCES tbl_kategori (id)
);