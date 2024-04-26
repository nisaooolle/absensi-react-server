package com.example.absensireact.config;

import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class AppConfig extends WebMvcConfigurerAdapter {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Replace with your specific frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE" ,"OPTIONS")
                .allowedHeaders("X-Requested-With", "auth-tgh" ,"Content-Type", "Origin", "Authorization", "Accept", "Client-Security-Token", "Accept-Encoding")
                .allowCredentials(true);
    }



}
