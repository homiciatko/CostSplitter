package com.agh.inzynierka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableOAuth2Client
@EnableAuthorizationServer
@Order(6)
public class InzynierkaApp {

    public static void main(String[] args) {
        SpringApplication.run(InzynierkaApp.class, args);
    }
}
