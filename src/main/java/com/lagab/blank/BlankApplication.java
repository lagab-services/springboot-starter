package com.lagab.blank;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import com.lagab.blank.common.config.CommonProperties;
import com.lagab.blank.config.ApplicationProperties;

import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties({ CommonProperties.class, ApplicationProperties.class,
        LiquibaseProperties.class })

@SpringBootApplication
@Slf4j
public class BlankApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BlankApplication.class);
        Environment env = app.run(args).getEnvironment();
        try {
            log.info("""
                            	
                            ----------------------------------------------------------
                            	\
                            Application '{}' is running! Access URLs:
                            	\
                            Local: 		http://localhost:{}
                            	\
                            External: 	http://{}:{}
                            	\
                            Profile(s): 	{}
                            ----------------------------------------------------------\
                            """,
                    env.getProperty("spring.application.name"), env.getProperty("server.port", "8080"),
                    InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port", "8080"),
                    env.getActiveProfiles());
        } catch (UnknownHostException e) {
            log.error("Unable to retrieve localhost address", e);
            System.exit(-1);
        }
    }

}
