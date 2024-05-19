package org.example.distancedata.service_implementation;

import org.example.distancedata.entity.Country;
import org.example.distancedata.services.implementation.DistanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DistanceServiceImplTest {

    @InjectMocks
    private DistanceServiceImpl service;

    @Test
    void shouldReturnNegativeOneForNullCountries() {
        double distance = service.getDistanceInKilometres(null, null);
        assertEquals(-1.0, distance, "Distance should be -1.0 for null inputs");
    }

    @Test
    void shouldReturnZeroForIdenticalCountries() {
        Country country1 = new Country();
        country1.setId(1L);
        country1.setName("CountryA");
        country1.setLatitude(10.0);
        country1.setLongitude(10.0);
        double distance = service.getDistanceInKilometres(country1, country1);
        assertEquals(0.0, distance, "Distance should be 0.0 for identical countries");
    }

    @Test
    void shouldCalculateDistanceForDifferentCountries() {
        Country country1 = new Country();
        country1.setId(1L);
        country1.setName("CountryA");
        country1.setLatitude(10.0);
        country1.setLongitude(10.0);
        Country country2 = new Country();
        country2.setId(2L);
        country2.setName("CountryB");
        country2.setLatitude(30.0);
        country2.setLongitude(40.0);

        double expectedDistance = 3821.2477; // Calculated using the Haversine formula
        double actualDistance = service.getDistanceInKilometres(country1, country2);

        assertEquals(expectedDistance, actualDistance, 0.1, "Calculated distance should be correct");
    }

    @Test
    void shouldCalculateDistanceForCountriesInDifferentHemisphere() {
        Country country1 = new Country(); // Sydney, Australia
        country1.setId(1L);
        country1.setName("CountryA");
        country1.setLatitude(-34.0);
        country1.setLongitude(151.0);
        Country country2 = new Country(); // New York, USA
        country2.setId(2L);
        country2.setName("CountryB");
        country2.setLatitude(40.0);
        country2.setLongitude(-74.0);

        double expectedDistance = 15988.8206; // Approximate distance using the Haversine formula
        double actualDistance = service.getDistanceInKilometres(country1, country2);

        assertEquals(expectedDistance, actualDistance, 100.0, "Calculated distance should be approximately correct");
    }

    @Test
    void shouldCalculateDistanceForCloseCountries() {
        Country country1 = new Country(); // London, UK
        country1.setId(1L);
        country1.setName("CountryA");
        country1.setLatitude(50.0);
        country1.setLongitude(0.0);
        Country country2 = new Country(); // Paris, France
        country2.setId(2L);
        country2.setName("CountryB");
        country2.setLatitude(48.0);
        country2.setLongitude(2.0);

        double expectedDistance = 265.9355; // Approximate distance using the Haversine formula
        double actualDistance = service.getDistanceInKilometres(country1, country2);

        assertEquals(expectedDistance, actualDistance, 10.0, "Calculated distance should be correct for close countries");
    }

    @Test
    void shouldCalculateDistanceForCountriesAtEquator() {
        Country country1 = new Country(); // Equatorial point
        country1.setId(1L);
        country1.setName("CountryA");
        country1.setLatitude(0.0);
        country1.setLongitude(0.0);
        Country country2 = new Country(); // Another equatorial point
        country2.setId(2L);
        country2.setName("CountryB");
        country2.setLatitude(0.0);
        country2.setLongitude(90.0);

        double expectedDistance = 10007.5434; // Approximate distance at the equator
        double actualDistance = service.getDistanceInKilometres(country1, country2);

        assertEquals(expectedDistance, actualDistance, 10.0, "Calculated distance should be correct for equatorial points");
    }
}
