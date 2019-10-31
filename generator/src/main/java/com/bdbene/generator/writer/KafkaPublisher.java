package com.bdbene.generator.writer;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaPublisher {
    private final KafkaProducer<String, Object> producer;

    @Autowired
    public KafkaPublisher(Properties kafkaProps) {
        Preconditions.checkNotNull(kafkaProps, "Kafka properties must be provided");
        
        producer = new KafkaProducer<>(kafkaProps);
    }

    public void writeData(List<SpecificRecord> data, String topic) {
        List<Future<RecordMetadata>> results =  data.stream()
            .map((SpecificRecord message) -> new ProducerRecord<String, Object>(topic, null, message))
            .map(record -> producer.send(record))
            .collect(Collectors.toList());

        long successes = results.stream()
            .flatMap(result -> checkResult(result).stream())
            .count();

        long dataSize = data.size();

        if (successes < dataSize) {
            log.warn("Wrote {} out of {} messages to topic {}.", successes, dataSize, topic);
        }
    }

    private Optional<RecordMetadata> checkResult(Future<RecordMetadata> result) {
        try {
            return Optional.of(result.get());
        } catch (CancellationException | ExecutionException e) {
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
}