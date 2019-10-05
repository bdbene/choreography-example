package com.bdbene.generator.datagen.serdes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bdbene.generator.exception.DataGenException;
import com.bdbene.generator.model.Person;
import com.bdbene.generator.model.Place;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileParser<T> {
    public List<T> readData(String fileName, Function<String[], Optional<T>> serializer) {
        log.info("Reading from file: '{}", fileName);
        
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            return stream.map(str -> str.split(","))
                    .flatMap(args -> serializer.apply(args).stream())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataGenException("Failed to read file " + fileName, e);
        }
    }

    public Optional<Person> createPerson(String[] args) {
        int length = args.length;
        if (length != 3) {
            log.warn("Found bad entry: {}", (Object[]) args);
            return Optional.empty();
        }

        for (int i = 0; i < length; ++i) {
            args[i] = args[i].trim();
        }

        Person person = Person.newBuilder()
            .setFirstName(args[0])
            .setLastName(args[1])
            .setEmail(args[2])
            .build();

        return Optional.of(person);
    }

    public Optional<Place> createPlace(String[] args) {
        int length = args.length;
        if (length != 6) {
            log.warn("Found bad entry: {}", (Object[]) args);
            return Optional.empty();
        }

        for (int i = 0; i < length; ++i) {
            args[i] = args[i].trim();
        }

        Place place = Place.newBuilder()
            .setAddress(args[0])
            .setCity(args[1])
            .setState(args[2])
            .setPostalCode(args[3])
            .setCountry(args[4])
            .setPhone(args[5])
            .build();

        return Optional.of(place);
    }
}