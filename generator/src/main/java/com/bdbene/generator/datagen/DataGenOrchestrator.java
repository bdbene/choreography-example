package com.bdbene.generator.datagen;

import java.util.List;

import javax.annotation.PostConstruct;

import com.bdbene.generator.model.Customer;
import com.bdbene.generator.model.Order;
import com.bdbene.generator.model.Payment;
import com.bdbene.generator.writer.KafkaPublisher;
import com.google.common.base.Preconditions;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataGenOrchestrator {
    private final PaymentGenerator paymentGenerator;
    private final OrderGenerator orderGenerator;
    private final CustomerGenerator customerGenerator;
    private final KafkaPublisher kafkaPublisher;
    
    @Autowired
    public DataGenOrchestrator(PaymentGenerator paymentGenerator, 
                                OrderGenerator orderGenerator, 
                                CustomerGenerator customerGenerator,
                                KafkaPublisher kafkaPublisher) {
        this.paymentGenerator = Preconditions.checkNotNull(paymentGenerator, "paymentGenerator must be provided");
        this.orderGenerator = Preconditions.checkNotNull(orderGenerator, "orderGenerator must be provided");
        this.customerGenerator = Preconditions.checkNotNull(customerGenerator, "customerGenerator must be provided");
        this.kafkaPublisher = Preconditions.checkNotNull(kafkaPublisher, "kafkaPublisher must be provided");
    }

    public void generateData() {
        List<Customer> customers = customerGenerator.generateData();
        List<SpecificRecord> orders = orderGenerator.generateData(customers);
        List<Payment> payments = paymentGenerator.generateData(orders);
/*
        customers.stream()
            .forEach(System.out::println);

        orders.stream()
            .forEach(System.out::println);

        payments.stream()
            .forEach(System.out::println);   */
            
        kafkaPublisher.writeData(orders, "ordersTopic");
    }

    @PostConstruct
    void init() {
        generateData();
    }
}