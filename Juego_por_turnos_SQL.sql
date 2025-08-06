-- Si existe, elimina la base de datos
USE master;
GO
IF DB_ID('videojuego_turnos') IS NOT NULL
BEGIN
    ALTER DATABASE videojuego_turnos SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE videojuego_turnos;
END
GO

-- Crea la base de datos
CREATE DATABASE videojuego_turnos;
GO

USE videojuego_turnos;
GO

-- Tabla: raza
CREATE TABLE raza (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);
GO

-- Tabla: arma
CREATE TABLE arma (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo VARCHAR(50),
    dano_minimo INT,
    dano_maximo INT,
    modificadores TEXT
);
GO

-- Tabla: personaje
CREATE TABLE personaje (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_raza INT NOT NULL,
    fuerza INT DEFAULT 10,
    energia INT DEFAULT 100,
    vida_actual INT NOT NULL,
    id_arma INT NOT NULL,
    FOREIGN KEY (id_raza) REFERENCES raza(id),
    FOREIGN KEY (id_arma) REFERENCES arma(id)
);
GO

-- Tabla: jugador
CREATE TABLE jugador (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    partidas_ganadas INT DEFAULT 0,
    partidas_perdidas INT DEFAULT 0
);
GO

-- Insertar razas
INSERT INTO raza (nombre, descripcion) VALUES
('Humano', 'Experto en armas de fuego'),
('Elfo', 'Maestro de la magia elemental'),
('Orco', 'Guerrero brutal con hachas y martillos'),
('Bestia', 'Híbrido animal con habilidades físicas extremas');

-- Insertar armas (solo las de Humano y Elfo para tu parte)
INSERT INTO arma (nombre, tipo, dano_minimo, dano_maximo, modificadores) VALUES
-- Humanos
('Escopeta', 'fuego', 1, 5, '+2% daño'),
('Rifle francotirador', 'fuego', 1, 5, 'daño aumenta a 10 si está a distancia'),
-- Elfos
('Báculo Fuego', 'fuego', 1, 5, '+10% daño'),
('Báculo Tierra', 'tierra', 1, 5, '+2%, mayor precisión'),
('Báculo Aire', 'aire', 1, 5, 'daño aumenta a 12 si está a distancia'),
('Báculo Agua', 'agua', 1, 5, 'sanación +90%, vida inicial 115');

ALTER TABLE personaje ADD id_jugador INT;
ALTER TABLE personaje ADD CONSTRAINT fk_jugador 
    FOREIGN KEY (id_jugador) REFERENCES jugador(id);

CREATE TABLE partida (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_jugador_ganador INT REFERENCES jugador(id),
    id_jugador_perdedor INT REFERENCES jugador(id),
    fecha DATETIME DEFAULT GETDATE(),
    razon VARCHAR(100) -- ej: "vida 0", "rendición"
); 

-- Crear tabla 'partida' para registrar resultados
CREATE TABLE partida (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_jugador_ganador INT NOT NULL,
    id_jugador_perdedor INT NOT NULL,
    fecha DATETIME DEFAULT GETDATE(),
    razon VARCHAR(100),
    FOREIGN KEY (id_jugador_ganador) REFERENCES jugador(id),
    FOREIGN KEY (id_jugador_perdedor) REFERENCES jugador(id)
);
GO

