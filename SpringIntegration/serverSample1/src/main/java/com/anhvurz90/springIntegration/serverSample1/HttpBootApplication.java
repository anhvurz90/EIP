package com.anhvurz90.springIntegration.serverSample1;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.splitter.DefaultMessageSplitter;

@SpringBootApplication
@EnableIntegrationManagement
public class HttpBootApplication {
    
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context
            = SpringApplication.run(HttpBootApplication.class, args);
        System.out.println("Visit http://localhost:8080/integration"
                + " to view the JSON object model");
        System.out.println("Hit enter to terminate");
        System.in.read();
        context.close();
    }

    @Bean
    public IntegrationFlow flow(RabbitTemplate rabbitTemplate) {
        return IntegrationFlows.from(
                    Http.inboundGateway("/receiveGateway")
                        .requestMapping(m -> m.methods(HttpMethod.POST))
                        .requestPayloadType(String.class))
                    .split(commaSplitter())
                    .<String, String>transform(p -> p + " from the other side")
//                    .<String, String>transform(String::toUpperCase)
                    .handle(Amqp.outboundGateway(rabbitTemplate).routingKey("queue1"))
                    .aggregate()
                    .transform(Object::toString)
                    .get();
    }
    
    @Bean
    public DefaultMessageSplitter commaSplitter() {
        DefaultMessageSplitter splitter = new DefaultMessageSplitter();
        splitter.setDelimiters(",");
        return splitter;
    }
    
    @Bean
    public IntegrationFlow amqp(ConnectionFactory connectionFactory) {
        return IntegrationFlows.from(Amqp.inboundGateway(connectionFactory, "queue1"))
                .<String, String>transform(String::toUpperCase)
                .get();
    }
}
