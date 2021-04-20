package com.epam.brest.web_app.config;


import com.epam.brest.test_db.SpringJdbcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.test_db", "com.epam.brest.service"})
@PropertySource({"classpath:dao.properties"})
public class ApplicationConfig {
}
