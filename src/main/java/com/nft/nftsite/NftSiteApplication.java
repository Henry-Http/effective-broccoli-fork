package com.nft.nftsite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.scheduling.annotation.EnableAsync;


@OpenAPIDefinition(
        info = @Info(
                title = "NFT Sale Website Application Backend Infrastructure",
                description = "This application handles the project's operations and persistence needs.",
                version = "v1",
                contact = @Contact(
                        name = "Adeola Adekunle",
                        email = "adeolaae1@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                url= "#",
                description = "Postman Documentation"
        ),
        servers = {
                @Server(url = "${swagger.server.url}", description = "Server URL"),
        },
        security = {
                @SecurityRequirement(
                        name = "Bearer Auth"
                )
        }
)
@SecurityScheme(
        name = "Bearer Auth",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
@Slf4j
@EnableAsync
public class NftSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftSiteApplication.class, args);
        log.info("::::::::: NFT SITE RUNNING :::::::::");
    }

}
