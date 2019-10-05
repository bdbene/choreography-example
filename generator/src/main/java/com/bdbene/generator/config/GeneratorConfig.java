package com.bdbene.generator.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfig {
    @Value("${data.location.people}")
    private String peopleDataLocation;

    @Value("${data.location.places}")
    private String placesDataLocation;

    public static final String PEOPLE_DATA_LOC = "peopleDataLoc";
    public static final String PLACES_DATA_LOC = "placesDataLoc";

    @Bean
    public Properties customerGenProps() {
        Properties props = new Properties();

        props.setProperty(PEOPLE_DATA_LOC, peopleDataLocation);
        props.setProperty(PLACES_DATA_LOC, placesDataLocation);

        return props;
    }
}