package org.example.distancedata.service_implementation;

import java.util.*;

import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.LanguageDTO;
import org.example.distancedata.entity.Language;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.LanguageRepository;
import org.example.distancedata.services.implementation.LanguageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LanguageServiceImplTest {
    @Mock
    private LanguageRepository repository;
    @Mock
    private LRUCache<Long, Language> cache;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private LanguageServiceImpl service;


    @Test
    public void shouldReturnAllLanguage() {
        List<Language> expectedLanguage = new ArrayList<>();
        when(repository.findAll(Sort.by("id")))
                .thenReturn(expectedLanguage);
        var actualCountries = service.read();
        assertEquals(expectedLanguage, actualCountries);
    }

    @Test
    public void findLanguageByIdNotInCache()
            throws ResourceNotFoundException {
        Long id = 5L;
        var expectedLanguage = Language.builder()
                .name("Russian").id(id).build();
        when(cache.get(id)).thenReturn(Optional.empty());
        when(repository.getLanguageById(id))
                .thenReturn(Optional.of(expectedLanguage));
        Language actualLanguage = service.getByID(id);
        assertEquals(expectedLanguage, actualLanguage);
        verify(cache, times(1))
                .put(id, expectedLanguage);
    }

    @Test
    public void findLanguageByIdNotExist() {
        Long id = 5L;
        when(cache.get(id)).thenReturn(Optional.empty());
        when(repository.getLanguageById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByID(id));
        verify(cache, never()).put(anyLong(), any(Language.class));
    }

    @Test
    public void findLanguageByIdInCache()
            throws ResourceNotFoundException {
        Long id = 5L;
        var expectedLanguage = Optional.of(new Language());
        when(cache.get(id)).thenReturn(expectedLanguage);
        var actualCountry = Optional.of(service.getByID(id));
        assertEquals(expectedLanguage, actualCountry);
        verify(repository, never()).findById(anyLong());
    }

    @Test
    public void findLanguageByNameNotInCache()
            throws ResourceNotFoundException {
        String name = "English";
        var expectedLanguage = Optional.of(new Language());
        when(repository.getByName(name)).thenReturn(expectedLanguage);
        var actualContinent = service.getByName(name);
        assertEquals(expectedLanguage.get(), actualContinent);
        verify(cache, times(1)).put(expectedLanguage.get().getId(), expectedLanguage.get());
    }

    @Test
    public void findLanguageByNameNotExist() {
        String name = "English";
        when(repository.getByName(name)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByName(name));
        verify(cache, never()).put(anyLong(), any(Language.class));
    }

    @Test
    public void createLanguageSuccessful() {
        var language = Language.builder()
                .name("English")
                .id(5L)
                .build();
        var actualLanguage = service.create(language);
        assertEquals(actualLanguage, language);
        verify(repository, times(1)).save(language);
        verify(cache, times(1))
                .put(anyLong(), any(Language.class));
    }


    @Test
    public void createLanguageByDtoSuccessful() {
        var newLanguage = LanguageDTO.builder()
                .name("Russian")
                .build();
        when(repository.getByName(newLanguage.getName()))
                .thenReturn(Optional.empty());
        when(repository.findAll(Sort.by("id")))
                .thenReturn(new ArrayList<>());
        service.create(newLanguage);
        verify(repository, times(1))
                .save(any(Language.class));
        verify(cache, times(1))
                .put(anyLong(), any(Language.class));
    }

    @Test
    public void createLanguageByDtoExistedId() {
        var newLanguage = LanguageDTO.builder()
                .name("Russian").build();
        when(repository.getByName(newLanguage.getName()))
                .thenReturn(Optional.of(new Language()));
        assertThrows(BadRequestException.class, () -> service.create(newLanguage));
        verify(repository, never())
                .save(any(Language.class));
        verify(cache, never())
                .put(anyLong(), any(Language.class));
    }

    @Test
    public void updateLanguageByEntity() throws ResourceNotFoundException {
        var newLanguage = Language.builder()
                .name("Test")
                .id(5L).build();
        when(repository.getLanguageById(newLanguage.getId()))
                .thenReturn(Optional.of(new Language()));
        service.update(newLanguage);
        verify(repository, times(1)).save(newLanguage);
        verify(cache, times(1))
                .remove(newLanguage.getId());
        verify(cache, times(1))
                .put(newLanguage.getId(), newLanguage);
    }

    @Test
    public void updateLanguageByNotExistedEntity() {
        var newLanguage = Language.builder()
                .name("Test")
                .id(5L).build();
        when(repository.getLanguageById(newLanguage.getId()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(newLanguage));
        verify(repository, never()).save(newLanguage);
        verify(cache, never())
                .remove(anyLong());
        verify(cache, never())
                .put(anyLong(), any(Language.class));
    }

    @Test
    public void updateLanguageByDto() throws ResourceNotFoundException {
        var language = LanguageDTO.builder()
                .name("Europe")
                .id(5L)
                .build();
        when(repository.getLanguageById(language.getId()))
                .thenReturn(Optional.of(new Language()));
        service.update(language);
        verify(repository, times(1))
                .save(any(Language.class));
        verify(cache, times(1))
                .remove(language.getId());
    }

    @Test
    public void updateLanguageByDtoWithNotExistedId() {
        var newLanguage = LanguageDTO.builder()
                .name("Europe")
                .id(5L).build();
        when(repository.getLanguageById(newLanguage.getId()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(newLanguage));
        verify(repository, never())
                .save(any(Language.class));
        verify(cache, never())
                .remove(anyLong());
    }

    @Test
    public void deleteLanguageValidId() throws ResourceNotFoundException {
        Long id = 5L;
        when(cache.get(id)).thenReturn(Optional.empty());
        when(repository.getLanguageById(id))
                .thenReturn(Optional.of(Language.builder()
                        .continents(new ArrayList<>())
                        .id(id).build()));
        service.delete(id);
        verify(cache, times(1))
                .remove(id);
        verify(repository, times(1))
                .deleteById(id);
    }

    @Test
    public void deleteLanguageInvalidId() {
        Long id = 5L;
        when(repository.getLanguageById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
        verify(repository, never()).deleteById(id);
    }

    @Test
    public void bulkInsert() {
        var firstLanguage = LanguageDTO.builder()
                .name("Europe")
                .id(1L)
                .build();
        var secondLanguage = LanguageDTO.builder()
                .name("Asia")
                .id(1L)
                .build();
        List<LanguageDTO> languagesDTOs = Arrays
                .asList(firstLanguage, secondLanguage);
        service.createBulk(languagesDTOs);
        String sql = "INSERT into language (name, id) VALUES (?, ?)";
        verify(jdbcTemplate, times(1))
                .batchUpdate(eq(sql), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void shouldFindNextAvailableIdWhenAllIdsUsed() {
        var existingLanguages = List.of(
                new Language(1L, "English"),
                new Language(2L, "French"),
                new Language(3L, "Spanish")
        );

        var usedIndexes = new HashSet<Long>(); // No used indexes provided

        when(service.read()).thenReturn(existingLanguages); // Mock read() response

        long freeId = service.findFreeId(usedIndexes);

        assertEquals(4L, freeId, "Next free ID should be 4");
    }

    @Test
    void shouldFindNextAvailableIdWhenThereAreGaps() {
        var existingLanguages = List.of(
                new Language(1L, "English"),
                new Language(3L, "French"),
                new Language(5L, "Spanish")
        );

        var usedIndexes = new HashSet<Long>(); // No used indexes provided

        when(service.read()).thenReturn(existingLanguages);

        long freeId = service.findFreeId(usedIndexes);

        assertEquals(2L, freeId, "Next free ID should be 2");
    }

    @Test
    void shouldConsiderUsedIndexes() {
        var existingLanguages = List.of(
                new Language(1L, "English"),
                new Language(2L, "French"),
                new Language(3L, "Spanish")
        );

        var usedIndexes = new HashSet<Long>(List.of(4L)); // Index 4 is already used

        when(service.read()).thenReturn(existingLanguages);

        long freeId = service.findFreeId(usedIndexes);

        assertEquals(5L, freeId, "Next free ID should be 5");
    }


}