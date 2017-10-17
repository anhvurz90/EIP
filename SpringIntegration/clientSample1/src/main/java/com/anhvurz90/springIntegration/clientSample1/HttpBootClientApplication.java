package com.anhvurz90.springIntegration.clientSample1;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;

@SpringBootApplication
@IntegrationComponentScan
public class HttpBootClientApplication {
    
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context
            = SpringApplication.run(HttpBootClientApplication.class, args);
        Gateway gate = context.getBean(Gateway.class);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            System.out.println(gate.exchange(line));
        }
        scanner.close();
    }
    
    @MessagingGateway(defaultRequestChannel = "httpOut.input")
    public interface Gateway {
        public String exchange(String out);
    }
    
    @Bean
    public IntegrationFlow httpOut() {
        return f -> f.handle(Http.outboundGateway("http://localhost:8080/receiveGateway")
                                 .expectedResponseType(String.class));
    }
}
