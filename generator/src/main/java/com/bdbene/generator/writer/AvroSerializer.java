package com.bdbene.generator.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvroSerializer {
    public static byte[] serialize(SpecificRecord record) {
        DatumWriter<SpecificRecord> writer = new SpecificDatumWriter<>(record.getSchema());
        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder encoder = null;

        try {
            encoder = EncoderFactory.get().binaryEncoder(stream, null);

            writer.write(record, encoder);
            encoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            log.error("Failed to serialize message: " + record.toString(), e);
        }

        return data;
    }
}