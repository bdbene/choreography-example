package com.bdbene.generator.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import com.bdbene.generator.config.GeneratorConfig;
import com.bdbene.generator.datagen.serdes.FileParser;
import com.bdbene.generator.model.Customer;
import com.bdbene.generator.model.Person;
import com.bdbene.generator.model.Place;
import com.google.common.base.Preconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
class CustomerGenerator {
    private final String peopleDataLoc;
    private final String placesDataLoc;
    private final int numCustomers;
    private final Random rng;

    private List<Place> cachedPlaces;
    private List<Person> cachedPeople;

    @Autowired
    public CustomerGenerator(Properties customerGenProps) {
        Preconditions.checkNotNull(customerGenProps, "Must provide properties");
        this.peopleDataLoc = Preconditions.checkNotNull(customerGenProps.getProperty(GeneratorConfig.PEOPLE_DATA_LOC),
                                "Must provide location for people data");
        this.placesDataLoc = Preconditions.checkNotNull(customerGenProps.getProperty(GeneratorConfig.PLACES_DATA_LOC),
                                "Must provide location for places data");

        this.numCustomers = Integer.parseInt(customerGenProps.getProperty(GeneratorConfig.NUM_CUSTOMERS));
    
        rng = new Random();
    }

    public List<Customer> generateData() {
        List<Customer> generatedData = new ArrayList<>(numCustomers);

        getData();

        for (int i = 0; i < numCustomers; ++i) {
            int peopleIndex = rng.nextInt(cachedPeople.size());
            int placesIndex = rng.nextInt(cachedPlaces.size());

            Person person = cachedPeople.get(peopleIndex);
            Place place = cachedPlaces.get(placesIndex);

            Customer customer = Customer.newBuilder()
                .setCustomerId(UUID.randomUUID().toString())
                .setFirstName(person.getFirstName())
                .setLastName(person.getLastName())
                .setEmail(person.getEmail())
                .setPhone(place.getPhone())
                .setAddress(place.getAddress())
                .setCity(place.getCity())
                .setState(place.getState())
                .setPostalCode(place.getPostalCode())
                .setCountry(place.getCountry())
                .build();

            generatedData.add(customer);
        }

        return generatedData;
    }

    private void getData() {
        FileParser<Place> placeDeserializer = new FileParser<>();
        FileParser<Person> personDeserializer = new FileParser<>();

        cachedPlaces = placeDeserializer.readData(placesDataLoc, placeDeserializer::createPlace);
        cachedPeople = personDeserializer.readData(peopleDataLoc, personDeserializer::createPerson);
    }
}