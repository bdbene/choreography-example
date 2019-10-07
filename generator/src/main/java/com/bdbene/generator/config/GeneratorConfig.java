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

    @Value("${generate.customers}")
    private String numCustomers;

    @Value("${generate.orders}")
    private String numOrders;

    @Value("${generate.payment.success}")
    private String paymentSuccess;

    @Value("${generate.payment.attempt}")
    private String paymentAttempt;

    public static final String PEOPLE_DATA_LOC = "peopleDataLoc";
    public static final String PLACES_DATA_LOC = "placesDataLoc";
    public static final String NUM_CUSTOMERS = "numCustomers";
    public static final String NUM_ORDERS = "numOrders";
    public static final String PAYMENT_SUCCESS = "paymentSuccess";
    public static final String PAYMENT_ATTEMPT = "paymentAttempt";

    @Bean
    public Properties customerGenProps() {
        Properties props = new Properties();

        props.setProperty(PEOPLE_DATA_LOC, peopleDataLocation);
        props.setProperty(PLACES_DATA_LOC, placesDataLocation);
        props.setProperty(NUM_CUSTOMERS, numCustomers);

        return props;
    }

    @Bean
    public Properties orderGenProps() {
        Properties props = new Properties();

        props.setProperty(NUM_ORDERS, numOrders);

        return props;
    }

    @Bean
    public Properties paymentGenProps() {
        Properties props = new Properties();

        props.setProperty(PAYMENT_SUCCESS, paymentSuccess);
        props.setProperty(PAYMENT_ATTEMPT, paymentAttempt);

        return props;
    }
}