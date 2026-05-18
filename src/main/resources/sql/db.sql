-- ============================================================
-- Sistema de Gestión de Riesgos Agrícolas - Chacra de Camote
-- SQL Server - Esquema de Base de Datos
-- ============================================================

-- ============================================================
-- Limpieza de tablas previas (para asegurar recreación limpia)
-- ============================================================
IF OBJECT_ID('dbo.alertas', 'U') IS NOT NULL DROP TABLE dbo.alertas;
IF OBJECT_ID('dbo.monitoreos', 'U') IS NOT NULL DROP TABLE dbo.monitoreos;
IF OBJECT_ID('dbo.parcelas', 'U') IS NOT NULL DROP TABLE dbo.parcelas;
IF OBJECT_ID('dbo.usuarios', 'U') IS NOT NULL DROP TABLE dbo.usuarios;

-- ============================================================
-- Tablas
-- ============================================================

-- Tabla de Usuarios
CREATE TABLE usuarios (
    id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
    username                NVARCHAR(50)   NOT NULL UNIQUE,
    password                NVARCHAR(255)  NOT NULL,
    email                   NVARCHAR(100)  NOT NULL UNIQUE,
    nombre_completo         NVARCHAR(150)  NOT NULL,
    telefono                NVARCHAR(20)   NULL,
    direccion               NVARCHAR(MAX)  NULL,
    fecha_registro          DATETIME2      NOT NULL DEFAULT GETDATE(),
    fecha_ultima_actividad  DATETIME2      NULL,
    rol                     NVARCHAR(20)   NOT NULL DEFAULT 'AGRICULTOR'
                                CONSTRAINT chk_rol_usuario CHECK (rol IN ('ADMINISTRATIVO','AGRICULTOR','ESPECIALISTA')),
    estado                  NVARCHAR(20)   NOT NULL DEFAULT 'ACTIVO'
                                CONSTRAINT chk_estado_usuario CHECK (estado IN ('ACTIVO','INACTIVO','BLOQUEADO'))
);

-- Tabla de Parcelas
CREATE TABLE parcelas (
    id                          BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre                      NVARCHAR(100)  NOT NULL,
    ubicacion                   NVARCHAR(MAX)  NOT NULL,
    area                        FLOAT          NOT NULL,
    tipo_suelo                  NVARCHAR(50)   NULL,
    cultivo                     NVARCHAR(100)  NULL,
    descripcion                 NVARCHAR(MAX)  NULL,
    fecha_creacion              DATETIME2      NOT NULL DEFAULT GETDATE(),
    fecha_ultima_modificacion   DATETIME2      NULL,
    estado                      NVARCHAR(20)   NOT NULL DEFAULT 'ACTIVA'
                                    CONSTRAINT chk_estado_parcela CHECK (estado IN ('ACTIVA','INACTIVA','BAJO_MANTENIMIENTO')),
    usuario_id                  BIGINT         NOT NULL,
    CONSTRAINT fk_parcela_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de Monitoreos
CREATE TABLE monitoreos (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    fecha_monitoreo     DATETIME2      NOT NULL DEFAULT GETDATE(),
    temperatura         FLOAT          NULL,
    humedad             FLOAT          NULL,
    precipitacion       FLOAT          NULL,
    velocidad_viento    FLOAT          NULL,
    observaciones       NVARCHAR(MAX)  NULL,
    estado              NVARCHAR(20)   NOT NULL DEFAULT 'COMPLETADO'
                            CONSTRAINT chk_estado_monitoreo CHECK (estado IN ('PENDIENTE','COMPLETADO','CANCELADO')),
    parcela_id          BIGINT         NOT NULL,
    CONSTRAINT fk_monitoreo_parcela FOREIGN KEY (parcela_id)
        REFERENCES parcelas(id) ON DELETE CASCADE
);

-- Tabla de Alertas
CREATE TABLE alertas (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    tipo            NVARCHAR(20)   NOT NULL
                        CONSTRAINT chk_tipo_alerta CHECK (tipo IN ('PLAGA','SEQUIA','LLUVIA_INTENSA','CALOR_EXCESIVO','HUMEDAD_BAJA','ENFERMEDAD','OTRO')),
    mensaje         NVARCHAR(MAX)  NOT NULL,
    nivel_riesgo    NVARCHAR(10)   NOT NULL DEFAULT 'MEDIA'
                        CONSTRAINT chk_nivel_alerta CHECK (nivel_riesgo IN ('BAJA','MEDIA','ALTA','CRITICA')),
    fecha           DATETIME2      NOT NULL DEFAULT GETDATE(),
    estado          NVARCHAR(15)   NOT NULL DEFAULT 'PENDIENTE'
                        CONSTRAINT chk_estado_alerta CHECK (estado IN ('PENDIENTE','ATENDIDA','DESCARTADA')),
    parcela_id      BIGINT         NOT NULL,
    CONSTRAINT fk_alerta_parcela FOREIGN KEY (parcela_id)
        REFERENCES parcelas(id) ON DELETE CASCADE
);

-- ============================================================
-- Datos de ejemplo
-- ============================================================

-- Usuarios
INSERT INTO usuarios (username, password, email, nombre_completo, telefono, direccion, rol, estado)
VALUES
    ('admin',     'admin123',    'admin@camote.com',     'Administrador del Sistema', '999000001', 'Oficina Central - Lima',        'ADMINISTRATIVO', 'ACTIVO'),
    ('jperez',    'jperez123',   'jperez@camote.com',    'Juan Pérez García',         '999000002', 'Sector Norte - Chacra Camote',  'AGRICULTOR',     'ACTIVO'),
    ('mrodriguez','mrodriguez1', 'mrodriguez@camote.com','María Rodríguez López',     '999000003', 'Sector Sur - Chacra Camote',    'AGRICULTOR',     'ACTIVO'),
    ('cdiaz',     'cdiaz123',    'cdiaz@camote.com',     'Carlos Díaz Sánchez',       '999000004', 'Laboratorio Agrícola - Lima',   'ESPECIALISTA',   'ACTIVO');

-- Parcelas
INSERT INTO parcelas (nombre, ubicacion, area, tipo_suelo, cultivo, descripcion, estado, usuario_id)
VALUES
    ('Parcela Norte A',  'Sector Norte - Bloque A', 2.50, 'Arenoso',      'Camote Amarillo',  'Parcela principal del sector norte.',                        'ACTIVA',              2),
    ('Parcela Norte B',  'Sector Norte - Bloque B', 1.80, 'Franco',       'Camote Morado',    'Parcela secundaria con riego por goteo.',                    'ACTIVA',              2),
    ('Parcela Sur A',    'Sector Sur - Bloque A',   3.20, 'Arcilloso',    'Camote Amarillo',  'Parcela grande del sector sur, suelo pesado.',               'ACTIVA',              3),
    ('Parcela Sur B',    'Sector Sur - Bloque B',   2.10, 'Franco',       'Camote Blanco',    'Parcela experimental con nueva variedad.',                   'ACTIVA',              3),
    ('Parcela Central',  'Sector Central',           4.00, 'Arenoso',     'Camote Amarillo',  'Parcela central con problemas de temperatura.',              'BAJO_MANTENIMIENTO',  2),
    ('Parcela Este',     'Sector Este',              1.50, 'Franco',      NULL,               'Parcela en descanso, sin cultivo actual.',                   'INACTIVA',            3);

-- Monitoreos
INSERT INTO monitoreos (fecha_monitoreo, temperatura, humedad, precipitacion, velocidad_viento, observaciones, estado, parcela_id)
VALUES
    ('2025-05-10 08:00:00', 22.5, 65.0,  0.0, 12.0, 'Condiciones normales. Cultivo en buen estado.',                           'COMPLETADO', 1),
    ('2025-05-10 08:30:00', 28.0, 30.0,  0.0, 15.0, 'Humedad muy baja. Se recomienda riego urgente.',                          'COMPLETADO', 2),
    ('2025-05-10 09:00:00', 21.0, 70.0,  2.5, 10.0, 'Lluvia ligera registrada. Suelo bien hidratado.',                         'COMPLETADO', 3),
    ('2025-05-10 09:30:00', 23.0, 68.0,  0.0,  8.0, 'Sin novedades. Parcela en buenas condiciones.',                           'COMPLETADO', 4),
    ('2025-05-10 10:00:00', 31.0, 25.0,  0.0, 20.0, 'Temperatura alta y humedad crítica. Riesgo de estrés térmico.',           'COMPLETADO', 5),
    ('2025-05-11 08:00:00', 23.0, 63.0,  0.0, 11.0, 'Monitoreo matutino. Todo estable.',                                      'COMPLETADO', 1),
    ('2025-05-11 08:30:00', 27.5, 32.0,  0.0, 14.0, 'Humedad sigue baja a pesar de riego aplicado ayer.',                      'COMPLETADO', 2),
    ('2025-05-12 08:00:00', 22.0, 66.0,  5.0, 18.0, 'Lluvias moderadas durante la noche. Verificar drenaje.',                  'COMPLETADO', 1),
    ('2025-05-12 09:00:00', 20.0, 75.0, 12.0, 22.0, 'Lluvia intensa. Posible encharcamiento en zonas bajas.',                  'COMPLETADO', 3),
    ('2025-05-13 08:00:00', 24.0, 60.0,  0.0, 10.0, 'Pendiente de completar mediciones de la tarde.',                          'PENDIENTE',  1);

-- Alertas
INSERT INTO alertas (tipo, mensaje, nivel_riesgo, estado, parcela_id)
VALUES
    ('SEQUIA',         'Nivel de humedad crítico (30%). Se requiere riego inmediato.',                         'ALTA',    'PENDIENTE',   2),
    ('CALOR_EXCESIVO', 'Temperatura supera los 30°C. Riesgo de estrés térmico en el cultivo.',                 'ALTA',    'PENDIENTE',   5),
    ('PLAGA',          'Presencia de gorgojo del camote detectada en sector central.',                         'MEDIA',   'PENDIENTE',   5),
    ('LLUVIA_INTENSA', 'Pronóstico de lluvias intensas para los próximos 3 días.',                             'BAJA',    'PENDIENTE',   1),
    ('PLAGA',          'Signos tempranos de mosca blanca en hojas. Monitorear.',                               'MEDIA',   'ATENDIDA',    3),
    ('HUMEDAD_BAJA',   'Humedad del suelo por debajo del 35%. Riesgo de marchitamiento.',                     'ALTA',    'PENDIENTE',   2),
    ('ENFERMEDAD',     'Manchas oscuras en tubérculos. Posible pudrición por Fusarium.',                      'CRITICA', 'PENDIENTE',   5),
    ('SEQUIA',         'Tres días sin precipitaciones y humedad descendiendo.',                                'MEDIA',   'ATENDIDA',    4),
    ('LLUVIA_INTENSA', 'Lluvia de 12mm registrada. Verificar encharcamiento en parcela.',                     'BAJA',    'DESCARTADA',  3),
    ('CALOR_EXCESIVO', 'Variación brusca de temperatura nocturna registrada.',                                'BAJA',    'DESCARTADA',  4);