package org.example.distancedata.service_implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContinentServiceImplTest {
    @Mock
    private ContinentRepository continentRepository;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private LRUCache<Long, Continent> cache;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private ContinentServiceImpl service;

    @Test
    public void shouldReturnAllCountry() {
        List<Continent> expectedContinent = new ArrayList<>();
        when(continentRepository.findAll(Sort.by("id")))
                .thenReturn(expectedContinent);
        var actualCountries = service.read();
        assertEquals(expectedContinent, actualCountries);
    }

    @Test
    public void findCountryByIdNotInCache()
            throws ResourceNotFoundException {
        Long id = 2L;
        var expectedContinent = Optional.of(new Continent());
        when(cache.get(id)).thenReturn(Optional.empty());
        when(continentRepository.getContinentById(id)).thenReturn(expectedContinent);
        Continent actualContinent = service.getByID(id);
        assertEquals(expectedContinent.get(), actualContinent);
        verify(cache, times(1))
                .put(id, expectedContinent.get());
    }

    @Test
    public void findContinentByIdNotExist() {
        Long id = 2L;
        when(cache.get(id)).thenReturn(Optional.empty());
        when(continentRepository.getContinentById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByID(id));
        verify(continentRepository, never()).findById(anyLong());
        verify(cache, never())
                .put(anyLong(), any(Continent.class));
    }

    @Test
    public void findContinentByIdInCache()
            throws ResourceNotFoundException {
        Long id = 2L;
        var expectedContinent = Optional.of(new Continent());
        when(cache.get(id)).thenReturn(expectedContinent);
        var actualCountry = Optional.of(service.getByID(id));
        assertEquals(expectedContinent, actualCountry);
        verify(continentRepository, never()).findById(anyLong());
    }

    @Test
    public void findContinentByNameNotInCache()
            throws ResourceNotFoundException {
        String name = "Asia";
        var expectedContinent = Optional.of(new Continent());
        when(continentRepository.getByName(name)).thenReturn(expectedContinent);
        var actualContinent = service.getByName(name);
        assertEquals(expectedContinent.get(), actualContinent);
        verify(cache, times(1)).put(expectedContinent.get().getId(), expectedContinent.get());
    }

    @Test
    public void findCountryByNameNotExist() {
        String name = "Asia";
        when(continentRepository.getByName(name)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByName(name));
        verify(cache, never())
                .put(anyLong(), any(Continent.class));
    }

    @Test
    public void createContinentSuccessful() {
        var newContinent = Continent.builder()
                .name("Europe")
                .id(2L).build();
        var actualContinent = service.create(newContinent);
        assertEquals(actualContinent, newContinent);
        verify(continentRepository, times(1)).save(newContinent);
        verify(cache, times(1))
                .put(newContinent.getId(), newContinent);
    }

    @Test
    public void createContinentByDtoSuccessful() {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .build();
        when(continentRepository.getByName(newContinent.getName()))
                .thenReturn(Optional.empty());
        when(languageRepository.getByName(anyString()))
                .thenReturn(Optional.empty());
        when(continentRepository.findAll(Sort.by("id")))
                .thenReturn(new ArrayList<>());
        service.create(newContinent);
        verify(continentRepository, times(1))
                .save(any(Continent.class));
        verify(cache, times(1))
                .put(anyLong(), any(Continent.class));
    }

    @Test
    public void createContinentByDtoExistedId() {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .id(2L).build();
        when(continentRepository.getByName(newContinent.getName()))
                .thenReturn(Optional.of(new Continent()));
        assertThrows(BadRequestException.class, () -> service.create(newContinent));
        verify(continentRepository, never())
                .save(any(Continent.class));
        verify(cache, times(1))
                .remove(anyLong());
    }

    @Test
    public void updateContinentByEntity() {
        var newContinent= Continent.builder()
                .name("Test")
                .id(22L).build();
        service.update(newContinent);
        verify(continentRepository, times(1)).save(newContinent);
        verify(cache, times(1))
                .remove(newContinent.getId());
    }

    @Test
    public void updateCountryByDto() throws ResourceNotFoundException {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .build();
        when(continentRepository.getContinentById(newContinent.getId()))
                .thenReturn(Optional.of(new Continent()));
        when(languageRepository.getByName(anyString()))
                .thenReturn(Optional.empty());
        service.update(newContinent);
        verify(continentRepository, times(1))
                .save(any(Continent.class));
        verify(cache, times(1))
                .remove(newContinent.getId());
    }

    @Test
    public void updateContinentByDtoWithNotExistedId() {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .id(2L).build();
        when(continentRepository.getContinentById(newContinent.getId()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(newContinent));
        verify(continentRepository, never())
                .save(any(Continent.class));
        verify(cache, never())
                .remove(anyLong());
    }

    @Test
    public void deleteContinentValidId() throws ResourceNotFoundException {
        Long id = 2L;
        when(continentRepository.getContinentById(id))
                .thenReturn(Optional.of(new Continent()));
        when(cache.get(anyLong()))
                .thenReturn(Optional.empty());
        service.delete(id);
        verify(cache, times(1))
                .remove(id);
        verify(continentRepository, times(1))
                .deleteById(id);
    }

    @Test
    public void deleteContinentInvalidId() {
        Long id = 2L;
        when(continentRepository.getContinentById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
        verify(continentRepository, never())
                .deleteById(id);
        verify(cache, never())
                .remove(id);
    }

    @Test
    public void deleteLanguageButNotExistedId() {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .id(2L).build();
        when(continentRepository.getContinentById(newContinent.getId()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class
                , () -> service.modifyLanguage(newContinent, true));
        verify(cache, never())
                .put(anyLong(), any(Continent.class));
        verify(continentRepository, never())
                .save(any(Continent.class));
        verify(languageRepository, never())
                .getByName(anyString());
    }

    @Test
    public void createLanguageButNotExistedId() {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .id(2L).build();
        when(continentRepository.getContinentById(newContinent.getId()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class
                , () -> service.modifyLanguage(newContinent, true));
        verify(cache, never())
                .put(anyLong(), any(Continent.class));
        verify(continentRepository, never())
                .save(any(Continent.class));
        verify(languageRepository, never())
                .getByName(anyString());
    }

    @Test
    public void deleteLanguageInContinent() throws ResourceNotFoundException {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .id(2L).build();
        var existedContinent = Continent.builder()
                .name("Europe")
                .id(2L).build();
        when(languageRepository.getByName(anyString()))
                .thenReturn(Optional.empty());
        when(continentRepository.getContinentById(newContinent.getId()))
                .thenReturn(Optional.of(existedContinent));
        service.modifyLanguage(newContinent, true);
        verify(continentRepository, times(1))
                .save(any(Continent.class));
        verify(cache, times(1))
                .remove(newContinent.getId());
    }

    @Test
    public void createLanguageInContinent() throws ResourceNotFoundException {
        var newContinent = ContinentDTO.builder()
                .name("Europe")
                .languages(List.of("English"))
                .id(2L).build();
        var existedContinent = Continent.builder()
                .name("Europe")
                .id(2L).build();
        when(languageRepository.getByName(anyString()))
                .thenReturn(Optional.empty());
        when(continentRepository.getContinentById(newContinent.getId()))
                .thenReturn(Optional.of(existedContinent));
        service.modifyLanguage(newContinent, false);
        verify(continentRepository, times(1))
                .save(any(Continent.class));
        verify(cache, times(1))
                .remove(newContinent.getId());
    }

    @Test
    public void getByLanguage() throws ResourceNotFoundException {
        Long id = 2L;
        when(languageRepository.getLanguageById(id))
                .thenReturn(Optional.of(Language.builder().name("English")
                        .id(id).build()));
        when(continentRepository.findAllContinentWithLanguage(id))
                .thenReturn(new ArrayList<>());
        assertNotEquals(null, service.getByLanguage(id));
    }

    @Test
    public void getByNotExistedLanguage() {
        Long id = 2L;
        when(languageRepository.getLanguageById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByLanguage(id));
        verify(continentRepository, never())
                .findAllContinentWithLanguage(id);
    }

    @Test
    public void bulkInsert() {
        var firstContinent = ContinentDTO.builder()
                .name("Europe")
                .id(1L)
                .build();
        var secondContinent = ContinentDTO.builder()
                .name("Asia")
                .id(1L)
                .build();
        List<ContinentDTO> continentDTOS = Arrays
                .asList(firstContinent, secondContinent);
        service.createBulk(continentDTOS);
        String sql = "INSERT into continent (name, id) VALUES (?, ?)";
        verify(jdbcTemplate, times(1))
                .batchUpdate(eq(sql), any(BatchPreparedStatementSetter.class));
    }
}