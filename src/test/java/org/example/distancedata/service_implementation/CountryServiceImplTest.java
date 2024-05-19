package org.example.distancedata.service_implementation;

import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.CountryDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Country;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.CountryRepository;
import org.example.distancedata.services.implementation.CountryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private LRUCache<Long, Country> cache;

    @InjectMocks
    private CountryServiceImpl countryService;

    public CountryServiceImplTest() {
    }

    @Test
    void testCreateCountry() throws BadRequestException {
        Country newCountry = Country.builder().id(1L).name("Utopia").build();

        // Simulate successful creation
        countryService.create(newCountry);

        verify(countryRepository, times(1)).save(newCountry);
        verify(cache, times(1)).put(1L, newCountry);
    }

    @Test
    void testCreateCountryWithContinent() throws BadRequestException {
        Continent continent = new Continent(1L, "Europe");
        CountryDTO countryDTO = CountryDTO.builder().name("Atlantis").latitude(40.0).longitude(60.0).build();

        // Simulate the scenario where the country does not exist
        when(countryRepository.getCountryByName("Atlantis")).thenReturn(Optional.empty());

        Country createdCountry = countryService.createWithContinent(countryDTO, continent);

        assertNotNull(createdCountry);
        assertEquals("Atlantis", createdCountry.getName());
        assertEquals(continent, createdCountry.getContinent());
    }

    @Test
    void testCreateCountryWithContinentThrowsBadRequestException() {
        Continent continent = new Continent(1L, "Europe");
        CountryDTO countryDTO = CountryDTO.builder().name("Atlantis").latitude(40.0).longitude(60.0).build();

        // Simulate the scenario where the country already exists
        when(countryRepository.getCountryByName("Atlantis")).thenReturn(Optional.of(new Country()));

        assertThrows(BadRequestException.class, () -> countryService.createWithContinent(countryDTO, continent));
    }

    @Test
    void testGetCountryByName() throws ResourceNotFoundException {
        Country mockCountry = new Country();
        mockCountry.setId(1L);
        mockCountry.setName("Utopia");

        when(countryRepository.getCountryByName("Utopia")).thenReturn(Optional.of(mockCountry));

        Country country = countryService.getByName("Utopia");

        assertNotNull(country);
        assertEquals("Utopia", country.getName());
        verify(cache, times(1)).put(1L, mockCountry);
    }

    @Test
    void testGetCountryByNameThrowsResourceNotFoundException() {
        when(countryRepository.getCountryByName("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.getByName("Nonexistent"));
    }

    @Test
    void testGetCountryByID() throws ResourceNotFoundException {
        Country mockCountry = new Country();
        mockCountry.setId(1L);
        mockCountry.setName("Utopia");

        when(cache.get(1L)).thenReturn(Optional.of(mockCountry));

        Country country = countryService.getByID(1L);

        assertEquals("Utopia", country.getName());
    }

    @Test
     void testGetCountryByIDFromRepository() throws ResourceNotFoundException {
        Country mockCountry = new Country();
        mockCountry.setId(1L);
        mockCountry.setName("Utopia");

        when(cache.get(1L)).thenReturn(Optional.empty());
        when(countryRepository.getCountryById(1L)).thenReturn(Optional.of(mockCountry));

        Country country = countryService.getByID(1L);

        assertEquals("Utopia", country.getName());
        verify(cache, times(1)).put(1L, mockCountry);
    }

    @Test
    void testGetCountryByIDThrowsResourceNotFoundException() {
        when(cache.get(1L)).thenReturn(Optional.empty());
        when(countryRepository.getCountryById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.getByID(1L));
    }


    @Test
    void testUpdateCountryThrowsResourceNotFoundException() {
        CountryDTO countryDTO = CountryDTO.builder().id(1L).name("Utopia").build();

        when(cache.get(1L)).thenReturn(Optional.empty());
        when(countryRepository.getCountryById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.update(countryDTO));
    }

    @Test
     void testDeleteCountry() throws ResourceNotFoundException {
        Country mockCountry = new Country();
        mockCountry.setId(1L);
        mockCountry.setName("Utopia");


        when(countryRepository.getCountryById(1L)).thenReturn(Optional.of(mockCountry));

        countryService.delete(1L);

        verify(countryRepository, times(1)).deleteById(1L);
        verify(cache, times(1)).remove(1L);
    }

    @Test
    void testDeleteCountryThrowsResourceNotFoundException() {
        when(countryRepository.getCountryById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.delete(1L));
    }

    @Test
    void testFindFreeIdWithUsedIndexes() {
        Country mockCountry1 = new Country();
        mockCountry1.setId(1L);
        mockCountry1.setName("Country2");

        Country mockCountry2 = new Country();
        mockCountry2.setId(2L);
        mockCountry2.setName("Country2");


        when(countryService.read()).thenReturn(List.of(mockCountry1, mockCountry2));

        HashSet<Long> usedIndexes = new HashSet<>(List.of(1L, 2L, 3L));

        long freeId = countryService.findFreeId(usedIndexes);

        assertEquals(4L, freeId); // The next available ID after 1, 2, and 3
    }
}
