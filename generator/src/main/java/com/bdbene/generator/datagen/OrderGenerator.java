package com.bdbene.generator.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import com.bdbene.generator.config.GeneratorConfig;
import com.bdbene.generator.model.Customer;
import com.bdbene.generator.model.Order;
import com.google.common.base.Preconditions;

public class OrderGenerator {
    private final int numOrders;
    private final Random rng;

    public OrderGenerator(Properties orderGenProps) {
        Preconditions.checkNotNull(orderGenProps, "Order Properties must be provided");
    
        numOrders = Integer.parseInt(orderGenProps.getProperty(GeneratorConfig.NUM_ORDERS));
    
        rng = new Random();
    }

    public List<Order> generateData(List<Customer> customers) {
        List<Order> generatedOrders = new ArrayList<>(numOrders);
        int numCustomers = customers.size();

        for (int i = 0; i < numOrders; ++i) {
            int customerIndex = rng.nextInt(numCustomers);

            Order order = Order.newBuilder()
                .setOrderId(UUID.randomUUID().toString())
                .setCustomerId(customers.get(customerIndex).getCustomerId())
                .setProducts(new ArrayList<>())
                .build();

            generatedOrders.add(order);
        }

        return generatedOrders;
    }
    
}