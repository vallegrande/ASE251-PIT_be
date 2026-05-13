-- ============================================================
-- Sistema de Gestión de Riesgos Agrícolas - Chacra de Camote
-- SQL Server
-- ============================================================

-- Crear base de datos
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'camote_db')
BEGIN
    CREATE DATABASE camote_db;
END
GO

USE camote_db;
GO

-- Tabla de Parcelas
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='parcelas' AND xtype='U')
BEGIN
    CREATE TABLE parcelas (
        id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
        nombre                  NVARCHAR(100)  NOT NULL,
        area                    DECIMAL(10,2)  NOT NULL,
        ubicacion               NVARCHAR(200)  NOT NULL,
        estado                  NVARCHAR(20)   NOT NULL DEFAULT 'ACTIVO'
                                    CONSTRAINT chk_estado_parcela CHECK (estado IN ('ACTIVO','EN_RIESGO','INACTIVO')),
        humedad                 DECIMAL(5,2)   NULL,
        temperatura             DECIMAL(5,2)   NULL,
        fecha_siembra           DATE           NULL,
        fecha_cosecha_estimada  DATE           NULL,
        created_at              DATETIME2      DEFAULT GETDATE(),
        updated_at              DATETIME2      DEFAULT GETDATE()
    );
END
GO

-- Tabla de Alertas
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='alertas' AND xtype='U')
BEGIN
    CREATE TABLE alertas (
        id              BIGINT IDENTITY(1,1) PRIMARY KEY,
        parcela_id      BIGINT         NOT NULL,
        tipo            NVARCHAR(20)   NOT NULL
                            CONSTRAINT chk_tipo_alerta CHECK (tipo IN ('PLAGA','SEQUIA','LLUVIA_INTENSA','TEMPERATURA','OTRO')),
        nivel           NVARCHAR(10)   NOT NULL DEFAULT 'MEDIA'
                            CONSTRAINT chk_nivel_alerta CHECK (nivel IN ('BAJA','MEDIA','ALTA','CRITICA')),
        descripcion     NVARCHAR(MAX)  NOT NULL,
        estado          NVARCHAR(15)   NOT NULL DEFAULT 'PENDIENTE'
                            CONSTRAINT chk_estado_alerta CHECK (estado IN ('PENDIENTE','ATENDIDA','DESCARTADA')),
        fecha_alerta    DATETIME2      DEFAULT GETDATE(),
        fecha_atencion  DATETIME2      NULL,
        CONSTRAINT fk_alerta_parcela FOREIGN KEY (parcela_id)
            REFERENCES parcelas(id) ON DELETE CASCADE
    );
END
GO

-- ============================================================
-- Datos de ejemplo
-- ============================================================

-- Parcelas
INSERT INTO parcelas (nombre, area, ubicacion, estado, humedad, temperatura, fecha_siembra, fecha_cosecha_estimada)
VALUES
    ('Parcela Norte A',  2.50, 'Sector Norte - Bloque A', 'ACTIVO',    65.0, 22.5, '2025-01-15', '2025-05-15'),
    ('Parcela Norte B',  1.80, 'Sector Norte - Bloque B', 'EN_RIESGO', 30.0, 28.0, '2025-01-20', '2025-05-20'),
    ('Parcela Sur A',    3.20, 'Sector Sur - Bloque A',   'ACTIVO',    70.0, 21.0, '2025-02-01', '2025-06-01'),
    ('Parcela Sur B',    2.10, 'Sector Sur - Bloque B',   'ACTIVO',    68.0, 23.0, '2025-02-10', '2025-06-10'),
    ('Parcela Central',  4.00, 'Sector Central',          'EN_RIESGO', 25.0, 31.0, '2024-12-01', '2025-04-01'),
    ('Parcela Este',     1.50, 'Sector Este',             'INACTIVO',  NULL, NULL,  NULL,         NULL);
GO

-- Alertas
INSERT INTO alertas (parcela_id, tipo, nivel, descripcion, estado)
VALUES
    (2, 'SEQUIA',         'ALTA',  'Nivel de humedad crítico (30%). Se requiere riego inmediato.',           'PENDIENTE'),
    (5, 'TEMPERATURA',    'ALTA',  'Temperatura supera los 30°C. Riesgo de estrés térmico en el cultivo.',  'PENDIENTE'),
    (5, 'PLAGA',          'MEDIA', 'Presencia de gorgojo del camote detectada en sector central.',           'PENDIENTE'),
    (1, 'LLUVIA_INTENSA', 'BAJA',  'Pronóstico de lluvias intensas para los próximos 3 días.',              'PENDIENTE'),
    (3, 'PLAGA',          'MEDIA', 'Signos tempranos de mosca blanca en hojas. Monitorear.',                'ATENDIDA'),
    (4, 'TEMPERATURA',    'BAJA',  'Variación brusca de temperatura nocturna registrada.',                  'DESCARTADA');
GO
