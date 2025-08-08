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

-- ==================================================
-- Tabla: raza
-- Almacena las razas disponibles en el juego
-- ==================================================
CREATE TABLE raza (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);
GO

-- ==================================================
-- Tabla: arma
-- Almacena todas las armas del juego por tipo
-- ==================================================
CREATE TABLE arma (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo VARCHAR(50),
    dano_minimo INT,
    dano_maximo INT,
    modificadores TEXT
);
GO

-- ==================================================
-- Tabla: jugador
-- Almacena los jugadores y sus estadísticas
-- ==================================================
CREATE TABLE jugador (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    partidas_ganadas INT DEFAULT 0,
    partidas_perdidas INT DEFAULT 0
);
GO

-- ==================================================
-- Tabla: personaje
-- Almacena los personajes creados, asociados a un jugador
-- ==================================================
CREATE TABLE personaje (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_raza INT NOT NULL,
    fuerza INT DEFAULT 10,
    energia INT DEFAULT 100,
    vida_actual INT NOT NULL,
    id_arma INT NOT NULL,
    id_jugador INT NOT NULL,
    FOREIGN KEY (id_raza) REFERENCES raza(id),
    FOREIGN KEY (id_arma) REFERENCES arma(id),
    FOREIGN KEY (id_jugador) REFERENCES jugador(id)
);
GO

-- ==================================================
-- Tabla: partida
-- Registra el resultado de cada partida
-- ==================================================
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

-- ==================================================
-- Insertar razas
-- ==================================================
INSERT INTO raza (nombre, descripcion) VALUES
('Humano', 'Experto en armas de fuego'),
('Elfo', 'Maestro de la magia elemental'),
('Orco', 'Guerrero brutal con hachas y martillos'),
('Bestia', 'Híbrido animal con habilidades físicas extremas');
GO

-- ==================================================
-- Insertar armas para todas las razas
-- ==================================================
INSERT INTO arma (nombre, tipo, dano_minimo, dano_maximo, modificadores) VALUES
-- 🔫 Humanos
('Escopeta', 'fuego', 1, 5, '+2% daño'),
('Rifle francotirador', 'fuego', 1, 5, 'Daño aumenta a 5-10 si está a distancia'),

-- 🔮 Elfos
('Báculo Fuego', 'fuego', 1, 5, '+10% daño'),
('Báculo Tierra', 'tierra', 1, 5, '+2% daño, mayor precisión'),
('Báculo Aire', 'aire', 1, 5, 'Daño aumenta a 4-12 si está a distancia'),
('Báculo Agua', 'agua', 1, 5, 'Sanación +90%, vida inicial 115'),

-- ⚔️ Orcos
('Hacha', 'corte', 1, 5, 'Provoca sangrado: -3 vida por 2 turnos'),
('Martillo', 'golpe', 1, 5, 'Daño contundente'),

-- 🐾 Bestias
('Puños', 'fisico', 25, 25, 'Daño fijo 25, pero el atacante pierde 10 de vida'),
('Espada', 'corte', 1, 10, 'Daño aleatorio 1-10');
GO

-- ==================================================
-- (Opcional) Insertar jugadores de prueba
-- ==================================================
-- Estos datos permiten probar el juego sin registrar cada vez
INSERT INTO jugador (nombre, partidas_ganadas, partidas_perdidas) VALUES
('Carlos', 0, 0),
('Ana', 0, 0);
GO

-- ==================================================
-- Mensaje final
-- ==================================================
PRINT '✅ Base de datos "videojuego_turnos" creada con éxito.';
PRINT '➡️  Razas: 4 insertadas.';
PRINT '➡️  Armas: 10 insertadas.';
PRINT '➡️  Tablas: raza, arma, jugador, personaje, partida.';
PRINT '➡️  Listo para usar con JDBC.';