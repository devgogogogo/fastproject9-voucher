package com.fastcampus.projectvoucher.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class OpenApiConfig {

    //http://localhost:8080/swagger-ui/index.html#/   <<-- 실행하고 이걸 치면 나옴

    @Bean
    public OpenAPI openApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Practice Voucher API")
                        .description("Practice Voucher API")
                        .version(LocalDateTime.now().toString())
                );
    }

}
