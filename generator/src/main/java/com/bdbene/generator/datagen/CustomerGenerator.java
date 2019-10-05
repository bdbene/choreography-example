package com.bdbene.generator.datagen;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import com.bdbene.generator.config.GeneratorConfig;
import com.bdbene.generator.datagen.serdes.FileParser;
import com.bdbene.generator.model.Customer;
import com.bdbene.generator.model.Person;
import com.bdbene.generator.model.Place;
import com.google.common.base.Preconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomerGenerator {
    private final String peopleDataLoc;
    private final String placesDataLoc;

    @Autowired
    public CustomerGenerator(Properties customerGenProps) {
        Preconditions.checkNotNull(customerGenProps, "Must provide properties");
        this.peopleDataLoc = Preconditions.checkNotNull(customerGenProps.getProperty(GeneratorConfig.PEOPLE_DATA_LOC),
                                "Must provide location for people data");
        this.placesDataLoc = Preconditions.checkNotNull(customerGenProps.getProperty(GeneratorConfig.PLACES_DATA_LOC),
                                "Must provide location for places data");
    }

    public Collection<Customer> generateData() {
        Collection<Customer> generatedData = new HashSet<>();

        FileParser<Place> placeDeserializer = new FileParser<>();
        FileParser<Person> personDeserializer = new FileParser<>();

        List<Place> places = placeDeserializer.readData(placesDataLoc, placeDeserializer::createPlace);
        List<Person> people = personDeserializer.readData(peopleDataLoc, personDeserializer::createPerson);

        places.stream()
            .forEach(System.out::println);

        people.stream()
            .forEach(System.out::println);

        return generatedData;
    }

    @PostConstruct
    public void init() {
        generateData();
    }
}