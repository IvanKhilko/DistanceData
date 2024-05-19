package org.example.distancedata.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryTest {
    @Test
    public void testCountryConstructorWithArguments() {
        Long id = 1L;
        String name = "Example Country";
        Double latitude = 40.7128;
        Double longitude = -74.0060;

        Country country = new Country(id, name, latitude, longitude, null);

        assertEquals(id, country.getId(), "ID should match the expected value.");
        assertEquals(name, country.getName(), "Name should match the expected value.");
        assertEquals(latitude, country.getLatitude(), "Latitude should match the expected value.");
        assertEquals(longitude, country.getLongitude(), "Longitude should match the expected value.");
    }

    @Test
    public void testCountryDefaultConstructor() {
        Country country = new Country();

        assertNull(country.getId(), "ID should be null for default constructor.");
        assertNull(country.getName(), "Name should be null for default constructor.");
        assertNull(country.getLatitude(), "Latitude should be null for default constructor.");
        assertNull(country.getLongitude(), "Longitude should be null for default constructor.");
    }

    @Test
    public void testCountrySettersAndGetters() {
        Country country = new Country();

        Long newId = 2L;
        String newName = "New Country";
        Double newLatitude = 52.5200;
        Double newLongitude = 13.4050;

        country.setId(newId);
        country.setName(newName);
        country.setLatitude(newLatitude);
        country.setLongitude(newLongitude);

        assertEquals(newId, country.getId(), "ID should match the set value.");
        assertEquals(newName, country.getName(), "Name should match the set value.");
        assertEquals(newLatitude, country.getLatitude(), "Latitude should match the set value.");
        assertEquals(newLongitude, country.getLongitude(), "Longitude should match the set value.");
    }

    @Test
    public void testCountryProperties() {
        Country country = new Country();
        country.setId(1L);
        country.setName("Kenya");
        country.setLatitude(-1.286389);
        country.setLongitude(36.817223);

        assertEquals(1L, country.getId());
        assertEquals("Kenya", country.getName());
        assertEquals(-1.286389, country.getLatitude(), 0.000001);
        assertEquals(36.817223, country.getLongitude(), 0.000001);
    }

    @Test
    public void testCountryWithContinent() {
        Continent continent = new Continent(1L, "Africa");
        Country country = new Country(1L, "Kenya", -1.286389, 36.817223, continent);

        assertEquals("Africa", continent.getName());
        assertEquals("Kenya", country.getName());
        assertSame(continent, country.getContinent());
    }

    @Test
    public void testAddCountryToContinent() {
        Continent continent = new Continent(1L, "Africa");
        Country country = new Country();
        country.setName("Kenya");

        country.setContinent(continent);

        assertSame(continent, country.getContinent());
    }
}
