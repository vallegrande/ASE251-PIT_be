package com.camote.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API REST.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestión de Riesgos Agrícolas - Camote")
                        .version("1.0.0")
                        .description("API REST para la gestión de riesgos agrícolas en una chacra de camote. "
                                + "Permite gestionar parcelas, monitoreos ambientales y alertas de riesgo.")
                        .contact(new Contact()
                                .name("Equipo CamoteApp")
                                .email("soporte@camoteapp.com")));
    }
}
