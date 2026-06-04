CREATE TABLE IF NOT EXISTS productos (
                                         id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         sku           VARCHAR(30)  NOT NULL UNIQUE,
    nombre        VARCHAR(120) NOT NULL,
    descripcion   VARCHAR(500),
    precio        DECIMAL(12,2) NOT NULL,
    stock         INT NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL,
    version       BIGINT
    );