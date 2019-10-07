package com.bdbene.generator.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.bdbene.generator.config.GeneratorConfig;
import com.bdbene.generator.model.Order;
import com.bdbene.generator.model.Payment;
import com.google.common.base.Preconditions;

public class PaymentGenerator {
    private final int paymentSuccess;
    private final int paymentAttempt;

    private final Random rng;

    public PaymentGenerator(Properties paymentGenProps) {
        Preconditions.checkNotNull(paymentGenProps, "Payment Properties must be provided");

        paymentSuccess = Integer.parseInt(paymentGenProps.getProperty(GeneratorConfig.PAYMENT_SUCCESS));
        paymentAttempt = Integer.parseInt(paymentGenProps.getProperty(GeneratorConfig.PAYMENT_ATTEMPT));

        rng = new Random();
    }

    public List<Payment> generateData(List<Order> orders) {
        int numOrders = orders.size();
        List<Payment> payments = new ArrayList<>(numOrders);

        for (Order order : orders) {
            int chance = rng.nextInt(100) + 1;

            if (chance > paymentAttempt) {
                continue;
            }

            Payment payment = Payment.newBuilder()
                .setCustomerId(order.getCustomerId())
                .setOrderId(order.getOrderId())
                .setSuccess(chance <= paymentSuccess)
                .build();

            payments.add(payment);
        }

        return payments;
    }
}