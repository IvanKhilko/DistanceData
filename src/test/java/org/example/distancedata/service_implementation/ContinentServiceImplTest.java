package org.example.distancedata.service_implementation;

import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.ContinentDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Language;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.ContinentRepository;
import org.example.distancedata.repository.LanguageRepository;
import org.example.distancedata.services.implementation.ContinentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContinentServiceImplTest {

    @Mock
    private ContinentRepository continentRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private LRUCache<Long, Continent> cache;

    @InjectMocks
    private ContinentServiceImpl continentService;

    public ContinentServiceImplTest() {
    }


    @Test
    void testCreateThrowsBadRequestException() {
        var continent = ContinentDTO.builder().name("Asia").build();

        // Simulate that a continent with this name already exists
        when(continentRepository.getByName("Asia")).thenReturn(Optional.of(new Continent()));

        assertThrows(BadRequestException.class, () -> continentService.create(continent));
    }



    @Test
    void testGetByName() throws ResourceNotFoundException {
        var mockContinent = new Continent(1L, "Europe");

        when(continentRepository.getByName("Europe")).thenReturn(Optional.of(mockContinent));

        var result = continentService.getByName("Europe");

        assertEquals("Europe", result.getName());
        verify(cache, times(1)).put(1L, mockContinent);
    }

    @Test
    void testGetByNameThrowsResourceNotFoundException() {
        when(continentRepository.getByName("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> continentService.getByName("Nonexistent"));
    }

    @Test
    void testGetByID() throws ResourceNotFoundException {
        var mockContinent = new Continent(1L, "Africa");

        when(cache.get(1L)).thenReturn(Optional.of(mockContinent));

        var result = continentService.getByID(1L);

        assertEquals("Africa", result.getName());
    }

    @Test
    void testGetByIDFromRepository() throws ResourceNotFoundException {
        var mockContinent = new Continent(1L, "Africa");

        when(cache.get(1L)).thenReturn(Optional.empty());
        when(continentRepository.getContinentById(1L)).thenReturn(Optional.of(mockContinent));

        var result = continentService.getByID(1L);

        assertEquals("Africa", result.getName());
        verify(cache, times(1)).put(1L, mockContinent);
    }

    @Test
    void testGetByIDThrowsResourceNotFoundException() {
        when(cache.get(1L)).thenReturn(Optional.empty());
        when(continentRepository.getContinentById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> continentService.getByID(1L));
    }


    @Test
    void testDelete() throws ResourceNotFoundException {
        var mockContinent = new Continent(1L, "Asia");

        when(continentRepository.getContinentById(1L)).thenReturn(Optional.of(mockContinent));

        continentService.delete(1L);

        verify(continentRepository, times(1)).deleteById(1L);
        verify(cache, times(1)).remove(1L);
    }

    @Test
    void testDeleteThrowsResourceNotFoundException() {
        when(continentRepository.getContinentById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> continentService.delete(1L));
    }


    @Test
    void testModifyLanguage() throws ResourceNotFoundException {
        var mockLanguage = new Language(1L, "English");
        var mockContinent = new Continent(1L, "Africa");

        when(continentRepository.getContinentById(1L)).thenReturn(Optional.of(mockContinent));
        when(languageRepository.getByName("English")).thenReturn(Optional.of(mockLanguage));

        var continentDTO = ContinentDTO.builder()
                .id(1L)
                .languages(List.of("English"))
                .build();

        continentService.modifyLanguage(continentDTO, false); // Adding a language

        assertTrue(mockContinent.getLanguages().contains(mockLanguage));
    }

    @Test
    void testModifyLanguageThrowsResourceNotFoundException() {
        when(continentRepository.getContinentById(1L)).thenReturn(Optional.empty());

        var continentDTO = ContinentDTO.builder()
                .id(1L)
                .languages(List.of("English"))
                .build();

        assertThrows(ResourceNotFoundException.class, () -> continentService.modifyLanguage(continentDTO, false));
    }
}
