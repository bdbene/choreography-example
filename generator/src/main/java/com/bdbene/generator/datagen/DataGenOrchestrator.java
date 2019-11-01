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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataGenOrchestrator {
    private final long rate;
    private final PaymentGenerator paymentGenerator;
    private final OrderGenerator orderGenerator;
    private final CustomerGenerator customerGenerator;
    private final KafkaPublisher kafkaPublisher;

    private final String ordersTopic;
    private final String paymentTopic;

    private List<byte[]> customers;
    private List<byte[]> orders;
    private List<byte[]> payments;
    
    @Autowired
    public DataGenOrchestrator(PaymentGenerator paymentGenerator, 
                                OrderGenerator orderGenerator, 
                                CustomerGenerator customerGenerator,
                                KafkaPublisher kafkaPublisher,
                                Properties orchestratorProps) {
        this.paymentGenerator = Preconditions.checkNotNull(paymentGenerator, "paymentGenerator must be provided");
        this.orderGenerator = Preconditions.checkNotNull(orderGenerator, "orderGenerator must be provided");
        this.customerGenerator = Preconditions.checkNotNull(customerGenerator, "customerGenerator must be provided");
        this.kafkaPublisher = Preconditions.checkNotNull(kafkaPublisher, "kafkaPublisher must be provided");
        Preconditions.checkNotNull(orchestratorProps, "orchestrator configs must be provided");
        
        ordersTopic = orchestratorProps.getProperty(GeneratorConfig.ORDERS_TOPIC);
        paymentTopic = orchestratorProps.getProperty(GeneratorConfig.PAYMENT_TOPIC);
        rate = Long.parseLong(orchestratorProps.getProperty(GeneratorConfig.ORCHESTRATION_RATE));
    }

    private void generateData() {
        List<Customer> customersObjs = customerGenerator.generateData();
        List<Order> ordersObjs = orderGenerator.generateData(customersObjs);
        List<Payment> paymentsObjs = paymentGenerator.generateData(ordersObjs);

        orders = ordersObjs.stream()
            .map(AvroSerializer::serialize)
            .collect(Collectors.toList());

        payments = paymentsObjs.stream()
            .map(AvroSerializer::serialize)
            .collect(Collectors.toList());
    }

    private void writeToKafka(List<byte[]> message, String topic) {
        kafkaPublisher.writeData(message, topic);
    }

    private void run() {
        while (true) {
            generateData();

            writeToKafka(orders, ordersTopic);
            writeToKafka(payments, paymentTopic);

            try {
                Thread.sleep(rate);
            } catch (InterruptedException e) {
                log.error("InteruptedException - Shutting down.", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @PostConstruct
    void init() {
        run();
    }
}