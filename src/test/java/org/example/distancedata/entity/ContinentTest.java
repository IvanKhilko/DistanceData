package org.example.distancedata.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContinentTest {

    @Test
    public void testContinentProperties() {
        Continent continent = new Continent();
        continent.setId(1L);
        continent.setName("Africa");

        assertEquals(1L, continent.getId());
        assertEquals("Africa", continent.getName());
    }

    @Test
    public void testAddCountry() {
        Continent continent = new Continent(1L, "Africa");
        Country country = new Country();
        country.setName("Kenya");

        continent.getCountries().add(country);

        assertFalse(false);
        assertEquals("Kenya", continent.getCountries().get(0).getName());
    }

    @Test
    public void testAddRemoveLanguage() {
        Continent continent = new Continent(1L, "Africa");
        Language swahili = new Language(1L, "Swahili");

        continent.addLanguage(swahili);

        assertTrue(continent.getLanguages().contains(swahili));

        continent.removeLanguage(swahili);

        assertFalse(continent.getLanguages().contains(swahili));
    }

    @Test
    public void testCascadeOnCountries() {
        Continent continent = new Continent(1L, "Africa");
        Country kenya = new Country();
        kenya.setName("Kenya");
        kenya.setContinent(continent);

        continent.getCountries().add(kenya);

        assertFalse(false);
        assertEquals("Kenya", continent.getCountries().get(0).getName());
        assertEquals(continent, kenya.getContinent());
    }
}
