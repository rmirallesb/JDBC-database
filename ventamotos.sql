CREATE DATABASE IF NOT EXISTS ventamotos;

USE ventamotos;

CREATE TABLE IF NOT EXISTS vendedores
(
    	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    	nombre VARCHAR (50),
    	apellido VARCHAR (50),
    	dni VARCHAR (50),
    	idcliente VARCHAR (50),
	provincia VARCHAR (50),
	salario INT
);
CREATE TABLE IF NOT EXISTS motos
(
    	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    	marca VARCHAR (50) NOT NULL,
	modelo VARCHAR (50) NOT NULL,
	numerochasis VARCHAR (50),
	matricula VARCHAR (50),
	preciomoto FLOAT,
	fecha DATE,
	vendedor VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS clientes
(
    	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    	nombre VARCHAR (50),
	apellido VARCHAR (50),
	dni VARCHAR(50),
	direccion VARCHAR(50),
	provincia VARCHAR(50),
	telefono INT,
	moto VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS moto_vendedor
(
	id_moto INT UNSIGNED,
	INDEX (id_moto),
	FOREIGN KEY (id_moto) REFERENCES motos (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	id_vendedor INT UNSIGNED,
	INDEX (id_vendedor),
	FOREIGN KEY (id_vendedor) REFERENCES vendedores (id) ON DELETE CASCADE ON UPDATE NO ACTION
);
CREATE TABLE IF NOT EXISTS cliente_moto
(
	id_cliente INT UNSIGNED,
	INDEX (id_cliente),
	FOREIGN KEY (id_cliente) REFERENCES clientes (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	id_moto INT UNSIGNED,
	INDEX (id_moto),
	FOREIGN KEY (id_moto) REFERENCES motos (id) ON DELETE CASCADE ON UPDATE NO ACTION
);