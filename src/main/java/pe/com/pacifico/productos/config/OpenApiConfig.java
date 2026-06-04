package pe.com.pacifico.productos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productosOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Productos Service API")
                        .version("1.0.0")
                        .description("Microservicio reactivo para la gestion de productos"));
    }
}