package com.bdbene.generator.datagen;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.bdbene.generator.config.GeneratorConfig;
import com.bdbene.generator.model.Customer;
import com.bdbene.generator.model.Order;
import com.bdbene.generator.model.Payment;
import com.bdbene.generator.writer.AvroSerializer;
import com.bdbene.generator.writer.KafkaPublisher;
import com.google.common.base.Preconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataGenOrchestrator {
    private final PaymentGenerator paymentGenerator;
    private final OrderGenerator orderGenerator;
    private final CustomerGenerator customerGenerator;
    private final KafkaPublisher kafkaPublisher;

    private final String ordersTopic;
    private final String paymentTopic;
    
    @Autowired
    public DataGenOrchestrator(PaymentGenerator paymentGenerator, 
                                OrderGenerator orderGenerator, 
                                CustomerGenerator customerGenerator,
                                KafkaPublisher kafkaPublisher,
                                Properties targetsProps) {
        this.paymentGenerator = Preconditions.checkNotNull(paymentGenerator, "paymentGenerator must be provided");
        this.orderGenerator = Preconditions.checkNotNull(orderGenerator, "orderGenerator must be provided");
        this.customerGenerator = Preconditions.checkNotNull(customerGenerator, "customerGenerator must be provided");
        this.kafkaPublisher = Preconditions.checkNotNull(kafkaPublisher, "kafkaPublisher must be provided");
        Preconditions.checkNotNull(targetsProps, "targets for data must be provided");
        
        ordersTopic = targetsProps.getProperty(GeneratorConfig.ORDERS_TOPIC);
        paymentTopic = targetsProps.getProperty(GeneratorConfig.PAYMENT_TOPIC);
    }

    public void generateData() {
        List<Customer> customers = customerGenerator.generateData();
        List<Order> orders = orderGenerator.generateData(customers);
        List<Payment> payments = paymentGenerator.generateData(orders);

        List<byte[]> serializedOrders = orders.stream()
            .map(AvroSerializer::serialize)
            .collect(Collectors.toList());

        List<byte[]> serializedPayments = payments.stream()
            .map(AvroSerializer::serialize)
            .collect(Collectors.toList());
            
        kafkaPublisher.writeData(serializedOrders, ordersTopic);
        kafkaPublisher.writeData(serializedPayments, paymentTopic);
    }

    @PostConstruct
    void init() {
        generateData();
    }
}