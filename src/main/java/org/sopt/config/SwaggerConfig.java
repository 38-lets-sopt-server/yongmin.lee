package org.sopt.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Assignment API")
                        .version("v1")
                        .description("SOPT 과제 API 문서"))
                .servers(List.of(
                        new Server()
                                // 고정 주소를 쓰지 않고, 현재 접속한 주소 기준으로 요청
                                .url("/")
                                .description("Current Server")
                ));
    }
}
