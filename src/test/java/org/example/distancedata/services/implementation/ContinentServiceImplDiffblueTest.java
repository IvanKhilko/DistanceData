package org.example.distancedata.services.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.ContinentDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Country;
import org.example.distancedata.entity.Language;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.ContinentRepository;
import org.example.distancedata.repository.LanguageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ContinentServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ContinentServiceImplDiffblueTest {
    @MockBean
    private ContinentRepository continentRepository;

    @Autowired
    private ContinentServiceImpl continentServiceImpl;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private LRUCache<Long, Continent> lRUCache;

    @MockBean
    private LanguageRepository languageRepository;

    /**
     * Method under test: {@link ContinentServiceImpl#create(ContinentDTO)}
     */
    @Test
    void testCreate() throws BadRequestException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getByName(Mockito.<String>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.create(continentDto));
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getByName(eq("Name"));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#create(ContinentDTO)}
     */
    @Test
    void testCreate2() throws BadRequestException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getByName(Mockito.<String>any())).thenReturn(ofResult);
        doThrow(new BadRequestException("An error occurred")).when(lRUCache)
                .put(Mockito.<Long>any(), Mockito.<Continent>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.create(continentDto));
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(continentRepository).getByName(eq("Name"));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#create(ContinentDTO)}
     */
    @Test
    void testCreate3() throws BadRequestException {
        // Arrange
        Continent continent = mock(Continent.class);
        when(continent.getId()).thenReturn(1L);
        doNothing().when(continent).setCountries(Mockito.<List<Country>>any());
        doNothing().when(continent).setId(Mockito.<Long>any());
        doNothing().when(continent).setLanguages(Mockito.<Set<Language>>any());
        doNothing().when(continent).setName(Mockito.<String>any());
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getByName(Mockito.<String>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.create(continentDto));
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(lRUCache).remove(eq(1L));
        verify(continent).getId();
        verify(continent).setCountries(isA(List.class));
        verify(continent).setId(eq(1L));
        verify(continent).setLanguages(isA(Set.class));
        verify(continent).setName(eq("Name"));
        verify(continentRepository).getByName(eq("Name"));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#create(ContinentDTO)}
     */
    @Test
    void testCreate4() throws BadRequestException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);
        when(continentRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());
        Optional<Continent> emptyResult = Optional.empty();
        when(continentRepository.getByName(Mockito.<String>any())).thenReturn(emptyResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act
        continentServiceImpl.create(continentDto);

        // Assert
        verify(lRUCache).put(eq(2L), isA(Continent.class));
        verify(continentRepository).getByName(eq("Name"));
        verify(continentRepository).save(isA(Continent.class));
        verify(continentRepository).findAll(isA(Sort.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#create(Continent)}
     */
    @Test
    void testCreate5() {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        // Act
        Continent actualCreateResult = continentServiceImpl.create(continent2);

        // Assert
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(continentRepository).save(isA(Continent.class));
        assertSame(continent2, actualCreateResult);
    }

    /**
     * Method under test: {@link ContinentServiceImpl#create(Continent)}
     */
    @Test
    void testCreate6() {
        // Arrange
        doThrow(new BadRequestException("An error occurred")).when(lRUCache)
                .put(Mockito.<Long>any(), Mockito.<Continent>any());

        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.create(continent));
        verify(lRUCache).put(eq(1L), isA(Continent.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#create(Continent)}
     */
    @Test
    void testCreate7() {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());
        Continent continent2 = mock(Continent.class);
        when(continent2.getId()).thenReturn(1L);
        doNothing().when(continent2).setCountries(Mockito.<List<Country>>any());
        doNothing().when(continent2).setId(Mockito.<Long>any());
        doNothing().when(continent2).setLanguages(Mockito.<Set<Language>>any());
        doNothing().when(continent2).setName(Mockito.<String>any());
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        // Act
        Continent actualCreateResult = continentServiceImpl.create(continent2);

        // Assert
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(continent2).getId();
        verify(continent2).setCountries(isA(List.class));
        verify(continent2).setId(eq(1L));
        verify(continent2).setLanguages(isA(Set.class));
        verify(continent2).setName(eq("Name"));
        verify(continentRepository).save(isA(Continent.class));
        assertSame(continent2, actualCreateResult);
    }

    /**
     * Method under test: {@link ContinentServiceImpl#read()}
     */
    @Test
    void testRead() {
        // Arrange
        ArrayList<Continent> continentList = new ArrayList<>();
        when(continentRepository.findAll(Mockito.<Sort>any())).thenReturn(continentList);

        // Act
        List<Continent> actualReadResult = continentServiceImpl.read();

        // Assert
        verify(continentRepository).findAll(isA(Sort.class));
        assertTrue(actualReadResult.isEmpty());
        assertSame(continentList, actualReadResult);
    }

    /**
     * Method under test: {@link ContinentServiceImpl#read()}
     */
    @Test
    void testRead2() {
        // Arrange
        when(continentRepository.findAll(Mockito.<Sort>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.read());
        verify(continentRepository).findAll(isA(Sort.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent2);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continent3 = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act
        continentServiceImpl.update(continent3);

        // Assert
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
        verify(continentRepository).save(isA(Continent.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate2() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new BadRequestException("An error occurred")).when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continent2 = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.update(continent2));
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate3() throws ResourceNotFoundException {
        // Arrange
        Optional<Continent> emptyResult = Optional.empty();
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(emptyResult);
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continent = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> continentServiceImpl.update(continent));
        verify(continentRepository).getContinentById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate4() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent2);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);

        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult2 = Optional.of(language);
        when(languageRepository.getByName(Mockito.<String>any())).thenReturn(ofResult2);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        ArrayList<String> languages = new ArrayList<>();
        languages.add("foo");
        ContinentDTO continent3 = ContinentDTO.builder().id(1L).languages(languages).name("Name").build();

        // Act
        continentServiceImpl.update(continent3);

        // Assert
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
        verify(languageRepository).getByName(eq("foo"));
        verify(continentRepository).save(isA(Continent.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate5() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent2);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);

        Continent continent3 = new Continent();
        continent3.setCountries(new ArrayList<>());
        continent3.setId(1L);
        continent3.setLanguages(new HashSet<>());
        continent3.setName("Name");

        ArrayList<Continent> continents = new ArrayList<>();
        continents.add(continent3);

        Language language = new Language();
        language.setContinents(continents);
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult2 = Optional.of(language);
        when(languageRepository.getByName(Mockito.<String>any())).thenReturn(ofResult2);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        ArrayList<String> languages = new ArrayList<>();
        languages.add("foo");
        ContinentDTO continent4 = ContinentDTO.builder().id(1L).languages(languages).name("Name").build();

        // Act
        continentServiceImpl.update(continent4);

        // Assert
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
        verify(languageRepository).getByName(eq("foo"));
        verify(continentRepository).save(isA(Continent.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate6() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent2);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);

        Continent continent3 = new Continent();
        continent3.setCountries(new ArrayList<>());
        continent3.setId(1L);
        continent3.setLanguages(new HashSet<>());
        continent3.setName("Name");

        Continent continent4 = new Continent();
        continent4.setCountries(new ArrayList<>());
        continent4.setId(2L);
        continent4.setLanguages(new HashSet<>());
        continent4.setName("org.example.distancedata.entity.Continent");

        ArrayList<Continent> continents = new ArrayList<>();
        continents.add(continent4);
        continents.add(continent3);

        Language language = new Language();
        language.setContinents(continents);
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult2 = Optional.of(language);
        when(languageRepository.getByName(Mockito.<String>any())).thenReturn(ofResult2);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        ArrayList<String> languages = new ArrayList<>();
        languages.add("foo");
        ContinentDTO continent5 = ContinentDTO.builder().id(1L).languages(languages).name("Name").build();

        // Act
        continentServiceImpl.update(continent5);

        // Assert
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
        verify(languageRepository).getByName(eq("foo"));
        verify(continentRepository).save(isA(Continent.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(ContinentDTO)}
     */
    @Test
    void testUpdate7() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        when(languageRepository.getByName(Mockito.<String>any())).thenThrow(new BadRequestException("An error occurred"));

        ArrayList<String> languages = new ArrayList<>();
        languages.add("foo");
        ContinentDTO continent2 = ContinentDTO.builder().id(1L).languages(languages).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.update(continent2));
        verify(continentRepository).getContinentById(eq(1L));
        verify(languageRepository).getByName(eq("foo"));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(Continent)}
     */
    @Test
    void testUpdate8() {
        // Arrange
        Continent continent = new Continent();
        ArrayList<Country> countries = new ArrayList<>();
        continent.setCountries(countries);
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        // Act
        continentServiceImpl.update(continent2);

        // Assert that nothing has changed
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).save(isA(Continent.class));
        assertEquals("Name", continent2.getName());
        assertEquals(1L, continent2.getId().longValue());
        assertTrue(continent2.getLanguages().isEmpty());
        assertEquals(countries, continent2.getCountries());
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(Continent)}
     */
    @Test
    void testUpdate9() {
        // Arrange
        doThrow(new BadRequestException("An error occurred")).when(lRUCache).remove(Mockito.<Long>any());

        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.update(continent));
        verify(lRUCache).remove(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#update(Continent)}
     */
    @Test
    void testUpdate10() {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        Continent continent2 = mock(Continent.class);
        when(continent2.getId()).thenReturn(1L);
        doNothing().when(continent2).setCountries(Mockito.<List<Country>>any());
        doNothing().when(continent2).setId(Mockito.<Long>any());
        doNothing().when(continent2).setLanguages(Mockito.<Set<Language>>any());
        doNothing().when(continent2).setName(Mockito.<String>any());
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        // Act
        continentServiceImpl.update(continent2);

        // Assert that nothing has changed
        verify(lRUCache).remove(eq(1L));
        verify(continent2).getId();
        verify(continent2).setCountries(isA(List.class));
        verify(continent2).setId(eq(1L));
        verify(continent2).setLanguages(isA(Set.class));
        verify(continent2).setName(eq("Name"));
        verify(continentRepository).save(isA(Continent.class));
    }

    /**
     * Method under test:
     * {@link ContinentServiceImpl#modifyLanguage(ContinentDTO, boolean)}
     */
    @Test
    void testModifyLanguage() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        ArrayList<Country> countries = new ArrayList<>();
        continent.setCountries(countries);
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act
        continentServiceImpl.modifyLanguage(continentDto, true);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).save(isA(Continent.class));
        assertEquals("Name", continentDto.getName());
        assertEquals(1L, continentDto.getId().longValue());
        assertEquals(countries, continentDto.getLanguages());
    }

    /**
     * Method under test:
     * {@link ContinentServiceImpl#modifyLanguage(ContinentDTO, boolean)}
     */
    @Test
    void testModifyLanguage2() throws ResourceNotFoundException {
        // Arrange
        when(lRUCache.get(Mockito.<Long>any())).thenThrow(new BadRequestException("An error occurred"));
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.modifyLanguage(continentDto, true));
        verify(lRUCache).get(eq(1L));
    }

    /**
     * Method under test:
     * {@link ContinentServiceImpl#modifyLanguage(ContinentDTO, boolean)}
     */
    @Test
    void testModifyLanguage3() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        ArrayList<Country> countries = new ArrayList<>();
        continent.setCountries(countries);
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);
        Continent continent2 = mock(Continent.class);
        when(continent2.getId()).thenReturn(1L);
        doNothing().when(continent2).setCountries(Mockito.<List<Country>>any());
        doNothing().when(continent2).setId(Mockito.<Long>any());
        doNothing().when(continent2).setLanguages(Mockito.<Set<Language>>any());
        doNothing().when(continent2).setName(Mockito.<String>any());
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act
        continentServiceImpl.modifyLanguage(continentDto, true);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(continent2).getId();
        verify(continent2).setCountries(isA(List.class));
        verify(continent2).setId(eq(1L));
        verify(continent2).setLanguages(isA(Set.class));
        verify(continent2).setName(eq("Name"));
        verify(continentRepository).save(isA(Continent.class));
        assertEquals("Name", continentDto.getName());
        assertEquals(1L, continentDto.getId().longValue());
        assertEquals(countries, continentDto.getLanguages());
    }

    /**
     * Method under test:
     * {@link ContinentServiceImpl#modifyLanguage(ContinentDTO, boolean)}
     */
    @Test
    void testModifyLanguage4() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);

        Continent continent2 = new Continent();
        ArrayList<Country> countries = new ArrayList<>();
        continent2.setCountries(countries);
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent2);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Continent> emptyResult = Optional.empty();
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO continentDto = idResult.languages(new ArrayList<>()).name("Name").build();

        // Act
        continentServiceImpl.modifyLanguage(continentDto, true);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
        verify(continentRepository).save(isA(Continent.class));
        assertEquals("Name", continentDto.getName());
        assertEquals(1L, continentDto.getId().longValue());
        assertEquals(countries, continentDto.getLanguages());
    }

    /**
     * Method under test:
     * {@link ContinentServiceImpl#modifyLanguage(ContinentDTO, boolean)}
     */
    @Test
    void testModifyLanguage5() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        when(continentRepository.save(Mockito.<Continent>any())).thenReturn(continent);

        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(languageRepository.getByName(Mockito.<String>any())).thenReturn(ofResult);
        Continent continent2 = mock(Continent.class);
        doNothing().when(continent2).removeLanguage(Mockito.<Language>any());
        when(continent2.getId()).thenReturn(1L);
        doNothing().when(continent2).setCountries(Mockito.<List<Country>>any());
        doNothing().when(continent2).setId(Mockito.<Long>any());
        doNothing().when(continent2).setLanguages(Mockito.<Set<Language>>any());
        doNothing().when(continent2).setName(Mockito.<String>any());
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        Optional<Continent> ofResult2 = Optional.of(continent2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult2);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        ArrayList<String> languages = new ArrayList<>();
        languages.add("foo");
        ContinentDTO continentDto = ContinentDTO.builder().id(1L).languages(languages).name("Name").build();

        // Act
        continentServiceImpl.modifyLanguage(continentDto, true);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(continent2).getId();
        verify(continent2).removeLanguage(isA(Language.class));
        verify(continent2).setCountries(isA(List.class));
        verify(continent2).setId(eq(1L));
        verify(continent2).setLanguages(isA(Set.class));
        verify(continent2).setName(eq("Name"));
        verify(languageRepository).getByName(eq("foo"));
        verify(continentRepository).save(isA(Continent.class));
        assertEquals("Name", continentDto.getName());
        assertEquals(1, continentDto.getLanguages().size());
        assertEquals(1L, continentDto.getId().longValue());
    }

    /**
     * Method under test:
     * {@link ContinentServiceImpl#modifyLanguage(ContinentDTO, boolean)}
     */
    @Test
    void testModifyLanguage6() throws ResourceNotFoundException {
        // Arrange
        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(languageRepository.getByName(Mockito.<String>any())).thenReturn(ofResult);
        Continent continent = mock(Continent.class);
        doThrow(new BadRequestException("An error occurred")).when(continent).removeLanguage(Mockito.<Language>any());
        doNothing().when(continent).setCountries(Mockito.<List<Country>>any());
        doNothing().when(continent).setId(Mockito.<Long>any());
        doNothing().when(continent).setLanguages(Mockito.<Set<Language>>any());
        doNothing().when(continent).setName(Mockito.<String>any());
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult2 = Optional.of(continent);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult2);

        ArrayList<String> languages = new ArrayList<>();
        languages.add("foo");
        ContinentDTO continentDto = ContinentDTO.builder().id(1L).languages(languages).name("Name").build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.modifyLanguage(continentDto, true));
        verify(lRUCache).get(eq(1L));
        verify(continent).removeLanguage(isA(Language.class));
        verify(continent).setCountries(isA(List.class));
        verify(continent).setId(eq(1L));
        verify(continent).setLanguages(isA(Set.class));
        verify(continent).setName(eq("Name"));
        verify(languageRepository).getByName(eq("foo"));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() throws ResourceNotFoundException {
        // Arrange
        doNothing().when(continentRepository).deleteById(Mockito.<Long>any());

        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        // Act
        continentServiceImpl.delete(1L);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() throws ResourceNotFoundException {
        // Arrange
        when(lRUCache.get(Mockito.<Long>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.delete(1L));
        verify(lRUCache).get(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#delete(Long)}
     */
    @Test
    void testDelete3() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        doNothing().when(continentRepository).deleteById(Mockito.<Long>any());
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Continent> emptyResult = Optional.empty();
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Continent>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        // Act
        continentServiceImpl.delete(1L);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Continent.class));
        verify(lRUCache).remove(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
        verify(continentRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#delete(Long)}
     */
    @Test
    void testDelete4() throws ResourceNotFoundException {
        // Arrange
        Optional<Continent> emptyResult = Optional.empty();
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(emptyResult);
        Optional<Continent> emptyResult2 = Optional.empty();
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(emptyResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> continentServiceImpl.delete(1L));
        verify(lRUCache).get(eq(1L));
        verify(continentRepository).getContinentById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#getByLanguage(Long)}
     */
    @Test
    void testGetByLanguage() throws ResourceNotFoundException {
        // Arrange
        ArrayList<Continent> continentList = new ArrayList<>();
        when(continentRepository.findAllContinentWithLanguage(Mockito.<Long>any())).thenReturn(continentList);

        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(languageRepository.getLanguageById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        List<Continent> actualByLanguage = continentServiceImpl.getByLanguage(1L);

        // Assert
        verify(continentRepository).findAllContinentWithLanguage(eq(1L));
        verify(languageRepository).getLanguageById(eq(1L));
        assertTrue(actualByLanguage.isEmpty());
        assertSame(continentList, actualByLanguage);
    }

    /**
     * Method under test: {@link ContinentServiceImpl#getByLanguage(Long)}
     */
    @Test
    void testGetByLanguage2() throws ResourceNotFoundException {
        // Arrange
        when(continentRepository.findAllContinentWithLanguage(Mockito.<Long>any()))
                .thenThrow(new BadRequestException("An error occurred"));

        Language language = new Language();
        language.setContinents(new ArrayList<>());
        language.setId(1L);
        language.setName("Name");
        Optional<Language> ofResult = Optional.of(language);
        when(languageRepository.getLanguageById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.getByLanguage(1L));
        verify(continentRepository).findAllContinentWithLanguage(eq(1L));
        verify(languageRepository).getLanguageById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#getByLanguage(Long)}
     */
    @Test
    void testGetByLanguage3() throws ResourceNotFoundException {
        // Arrange
        Optional<Language> emptyResult = Optional.empty();
        when(languageRepository.getLanguageById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> continentServiceImpl.getByLanguage(1L));
        verify(languageRepository).getLanguageById(eq(1L));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk() throws BadRequestException, DataAccessException {
        // Arrange
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        // Act
        continentServiceImpl.createBulk(new ArrayList<>());

        // Assert that nothing has changed
        verify(jdbcTemplate).batchUpdate(eq("INSERT into continent (name) VALUES (?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk2() throws BadRequestException, DataAccessException {
        // Arrange
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ArrayList<ContinentDTO> list = new ArrayList<>();
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO buildResult = idResult.languages(new ArrayList<>()).name("Name").build();
        list.add(buildResult);

        // Act
        continentServiceImpl.createBulk(list);

        // Assert
        verify(jdbcTemplate).batchUpdate(eq("INSERT into continent (name) VALUES (?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk3() throws BadRequestException, DataAccessException {
        // Arrange
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ArrayList<ContinentDTO> list = new ArrayList<>();
        ContinentDTO.ContinentDTOBuilder idResult = ContinentDTO.builder().id(1L);
        ContinentDTO buildResult = idResult.languages(new ArrayList<>()).name("Name").build();
        list.add(buildResult);
        ContinentDTO.ContinentDTOBuilder idResult2 = ContinentDTO.builder().id(1L);
        ContinentDTO buildResult2 = idResult2.languages(new ArrayList<>()).name("Name").build();
        list.add(buildResult2);

        // Act
        continentServiceImpl.createBulk(list);

        // Assert
        verify(jdbcTemplate).batchUpdate(eq("INSERT into continent (name) VALUES (?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link ContinentServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk4() throws BadRequestException, DataAccessException {
        // Arrange
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> continentServiceImpl.createBulk(new ArrayList<>()));
        verify(jdbcTemplate).batchUpdate(eq("INSERT into continent (name) VALUES (?)"),
                isA(BatchPreparedStatementSetter.class));
    }
}
