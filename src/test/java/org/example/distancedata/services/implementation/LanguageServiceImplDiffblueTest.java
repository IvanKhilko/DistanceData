package org.example.distancedata.services.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Language;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.LanguageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LanguageServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class LanguageServiceImplDiffblueTest {
    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private LRUCache<Long, Language> lRUCache;

    @MockBean
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageServiceImpl languageServiceImpl;

    /**
     * Method under test: {@link LanguageServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() throws ResourceNotFoundException {
        // Arrange
        doNothing().when(languageRepository).deleteById(Mockito.<Long>any());

        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        // Act
        languageServiceImpl.delete(1L);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(languageRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link LanguageServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() throws ResourceNotFoundException {
        // Arrange
        when(lRUCache.get(Mockito.<Long>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> languageServiceImpl.delete(1L));
        verify(lRUCache).get(eq(1L));
    }

    /**
     * Method under test: {@link LanguageServiceImpl#delete(Long)}
     */
    @Test
    void testDelete3() throws ResourceNotFoundException {
        // Arrange
        doNothing().when(languageRepository).deleteById(Mockito.<Long>any());
        Language language = mock(Language.class);
        when(language.getId()).thenReturn(1L);
        when(language.getContinents()).thenReturn(new ArrayList<>());
        doNothing().when(language).setContinents(Mockito.<List<Continent>>any());
        doNothing().when(language).setId(Mockito.<Long>any());
        doNothing().when(language).setName(Mockito.<String>any());
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        // Act
        languageServiceImpl.delete(1L);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(language).getContinents();
        verify(language).getId();
        verify(language).setContinents(isA(List.class));
        verify(language).setId(eq(1L));
        verify(language).setName(eq("Name"));
        verify(languageRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link LanguageServiceImpl#delete(Long)}
     */
    @Test
    void testDelete4() throws ResourceNotFoundException {
        // Arrange
        doNothing().when(languageRepository).deleteById(Mockito.<Long>any());

        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        ArrayList<Continent> continentList = new ArrayList<>();
        continentList.add(continent);
        Language language = mock(Language.class);
        when(language.getId()).thenReturn(1L);
        when(language.getContinents()).thenReturn(continentList);
        doNothing().when(language).setContinents(Mockito.<List<Continent>>any());
        doNothing().when(language).setId(Mockito.<Long>any());
        doNothing().when(language).setName(Mockito.<String>any());
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        // Act
        languageServiceImpl.delete(1L);

        // Assert
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(language).getContinents();
        verify(language).getId();
        verify(language).setContinents(isA(List.class));
        verify(language).setId(eq(1L));
        verify(language).setName(eq("Name"));
        verify(languageRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link LanguageServiceImpl#delete(Long)}
     */
    @Test
    void testDelete5() throws ResourceNotFoundException {
        // Arrange
        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        doNothing().when(languageRepository).deleteById(Mockito.<Long>any());
        when(languageRepository.getLanguageById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Language> emptyResult = Optional.empty();
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Language>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        // Act
        languageServiceImpl.delete(1L);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Language.class));
        verify(lRUCache).remove(eq(1L));
        verify(languageRepository).getLanguageById(eq(1L));
        verify(languageRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link LanguageServiceImpl#findFreeId(HashSet)}
     */
    @Test
    void testFindFreeId() {
        // Arrange
        when(languageRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());

        // Act
        long actualFindFreeIdResult = languageServiceImpl.findFreeId(new HashSet<>());

        // Assert
        verify(languageRepository).findAll(isA(Sort.class));
        assertEquals(1L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link LanguageServiceImpl#findFreeId(HashSet)}
     */
    @Test
    void testFindFreeId2() {
        // Arrange
        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("id");

        ArrayList<Language> languageList = new ArrayList<>();
        languageList.add(language);
        when(languageRepository.findAll(Mockito.<Sort>any())).thenReturn(languageList);

        // Act
        long actualFindFreeIdResult = languageServiceImpl.findFreeId(new HashSet<>());

        // Assert
        verify(languageRepository).findAll(isA(Sort.class));
        assertEquals(2L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link LanguageServiceImpl#findFreeId(HashSet)}
     */
    @Test
    void testFindFreeId3() {
        // Arrange
        when(languageRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());

        HashSet<Long> usedIndexes = new HashSet<>();
        usedIndexes.add(1L);

        // Act
        long actualFindFreeIdResult = languageServiceImpl.findFreeId(usedIndexes);

        // Assert
        verify(languageRepository).findAll(isA(Sort.class));
        assertEquals(2L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link LanguageServiceImpl#findFreeId(HashSet)}
     */
    @Test
    void testFindFreeId4() {
        // Arrange
        when(languageRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());

        HashSet<Long> usedIndexes = new HashSet<>();
        usedIndexes.add(0L);
        usedIndexes.add(1L);

        // Act
        long actualFindFreeIdResult = languageServiceImpl.findFreeId(usedIndexes);

        // Assert
        verify(languageRepository).findAll(isA(Sort.class));
        assertEquals(2L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link LanguageServiceImpl#findFreeId(HashSet)}
     */
    @Test
    void testFindFreeId5() {
        // Arrange
        when(languageRepository.findAll(Mockito.<Sort>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> languageServiceImpl.findFreeId(new HashSet<>()));
        verify(languageRepository).findAll(isA(Sort.class));
    }

    /**
     * Method under test: {@link LanguageServiceImpl#findFreeId(HashSet)}
     */
    @Test
    void testFindFreeId6() {
        // Arrange
        Language language = mock(Language.class);
        when(language.getId()).thenReturn(1L);
        doNothing().when(language).setContinents(Mockito.<List<Continent>>any());
        doNothing().when(language).setId(Mockito.<Long>any());
        doNothing().when(language).setName(Mockito.<String>any());
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("id");

        ArrayList<Language> languageList = new ArrayList<>();
        languageList.add(language);
        when(languageRepository.findAll(Mockito.<Sort>any())).thenReturn(languageList);

        // Act
        long actualFindFreeIdResult = languageServiceImpl.findFreeId(new HashSet<>());

        // Assert
        verify(language).getId();
        verify(language).setContinents(isA(List.class));
        verify(language).setId(eq(1L));
        verify(language).setName(eq("id"));
        verify(languageRepository).findAll(isA(Sort.class));
        assertEquals(2L, actualFindFreeIdResult);
    }
}
