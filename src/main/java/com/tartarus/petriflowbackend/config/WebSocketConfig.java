package com.tartarus.petriflowbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebMvc
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final Environment environment;

    @Autowired
    public WebSocketConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String stompAllowedOrigins = environment.getProperty("stomp-allowed-origins");

        if (stompAllowedOrigins == null) {
            throw new IllegalStateException("Server property stomp-allowed-origins has to be set.");
        }

        registry.addEndpoint("/petri-net")
                .setAllowedOrigins(stompAllowedOrigins.split(","))
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowCredentials(false)
                        .maxAge(3600)
                        .allowedHeaders("*")
                        .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
            }
        };
    }

}
