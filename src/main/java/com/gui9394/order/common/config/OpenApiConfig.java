package com.gui9394.order.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customize() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order API")
                        .contact(new Contact()
                                .name("Paulo Silva")
                                .url("https://www.linkedin.com/in/gui9394/")));
    }

}
