package com.lyncode.dataencoder;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebStarter {
    public static ConfigurableApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext startApplication(String... profiles) {
        APPLICATION_CONTEXT = new SpringApplicationBuilder(WebStarter.class)
                .profiles(profiles)
                .run();
        return APPLICATION_CONTEXT;
    }

    public static void stopApplication () {
        APPLICATION_CONTEXT.stop();
    }

    public static void main (String... args) {
        startApplication(Profile.PRODUCTION);
    }

    public static class Profile {
        public static final String PRODUCTION = "production";
    }
}
