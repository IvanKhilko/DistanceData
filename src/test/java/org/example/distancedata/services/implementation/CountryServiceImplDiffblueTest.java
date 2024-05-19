package org.example.distancedata.services.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.CountryDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Country;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.ContinentRepository;
import org.example.distancedata.repository.CountryRepository;
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

@ContextConfiguration(classes = {CountryServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CountryServiceImplDiffblueTest {
    @MockBean
    private ContinentRepository continentRepository;

    @MockBean
    private CountryRepository countryRepository;

    @Autowired
    private CountryServiceImpl countryServiceImpl;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private LRUCache<Long, Country> lRUCache;

    /**
     * Method under test: {@link CountryServiceImpl#findFreeId()}
     */
    @Test
    void testFindFreeId() {
        // Arrange
        when(countryRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());

        // Act
        long actualFindFreeIdResult = countryServiceImpl.findFreeId();

        // Assert
        verify(countryRepository).findAll(isA(Sort.class));
        assertEquals(2L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link CountryServiceImpl#findFreeId()}
     */
    @Test
    void testFindFreeId2() {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("id");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("id");

        ArrayList<Country> countryList = new ArrayList<>();
        countryList.add(country);
        when(countryRepository.findAll(Mockito.<Sort>any())).thenReturn(countryList);

        // Act
        long actualFindFreeIdResult = countryServiceImpl.findFreeId();

        // Assert
        verify(countryRepository).findAll(isA(Sort.class));
        assertEquals(3L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link CountryServiceImpl#findFreeId()}
     */
    @Test
    void testFindFreeId3() {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("id");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("id");

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(2L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        Country country2 = new Country();
        country2.setContinent(continent2);
        country2.setId(2L);
        country2.setLatitude(0.5d);
        country2.setLongitude(0.5d);
        country2.setName("Name");

        ArrayList<Country> countryList = new ArrayList<>();
        countryList.add(country2);
        countryList.add(country);
        when(countryRepository.findAll(Mockito.<Sort>any())).thenReturn(countryList);

        // Act
        long actualFindFreeIdResult = countryServiceImpl.findFreeId();

        // Assert
        verify(countryRepository).findAll(isA(Sort.class));
        assertEquals(1L, actualFindFreeIdResult);
    }

    /**
     * Method under test: {@link CountryServiceImpl#findFreeId()}
     */
    @Test
    void testFindFreeId4() {
        // Arrange
        when(countryRepository.findAll(Mockito.<Sort>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> countryServiceImpl.findFreeId());
        verify(countryRepository).findAll(isA(Sort.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#findFreeId()}
     */
    @Test
    void testFindFreeId5() {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("id");
        Country country = mock(Country.class);
        when(country.getId()).thenReturn(1L);
        doNothing().when(country).setContinent(Mockito.<Continent>any());
        doNothing().when(country).setId(Mockito.<Long>any());
        doNothing().when(country).setLatitude(Mockito.<Double>any());
        doNothing().when(country).setLongitude(Mockito.<Double>any());
        doNothing().when(country).setName(Mockito.<String>any());
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("id");

        ArrayList<Country> countryList = new ArrayList<>();
        countryList.add(country);
        when(countryRepository.findAll(Mockito.<Sort>any())).thenReturn(countryList);

        // Act
        long actualFindFreeIdResult = countryServiceImpl.findFreeId();

        // Assert
        verify(country).getId();
        verify(country).setContinent(isA(Continent.class));
        verify(country).setId(eq(1L));
        verify(country).setLatitude(eq(10.0d));
        verify(country).setLongitude(eq(10.0d));
        verify(country).setName(eq("id"));
        verify(countryRepository).findAll(isA(Sort.class));
        assertEquals(3L, actualFindFreeIdResult);
    }

    /**
     * Method under test:
     * {@link CountryServiceImpl#updateWithContinent(CountryDTO, Continent)}
     */
    @Test
    void testUpdateWithContinent() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");
        when(countryRepository.save(Mockito.<Country>any())).thenReturn(country);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        Country country2 = new Country();
        country2.setContinent(continent2);
        country2.setId(1L);
        country2.setLatitude(10.0d);
        country2.setLongitude(10.0d);
        country2.setName("Name");
        Optional<Country> ofResult = Optional.of(country2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Country>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        CountryDTO country3 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        Continent continent3 = new Continent();
        continent3.setCountries(new ArrayList<>());
        continent3.setId(1L);
        continent3.setLanguages(new HashSet<>());
        continent3.setName("Name");

        // Act
        countryServiceImpl.updateWithContinent(country3, continent3);

        // Assert
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Country.class));
        verify(lRUCache).remove(eq(1L));
        verify(countryRepository).save(isA(Country.class));
    }

    /**
     * Method under test:
     * {@link CountryServiceImpl#updateWithContinent(CountryDTO, Continent)}
     */
    @Test
    void testUpdateWithContinent2() throws ResourceNotFoundException {
        // Arrange
        when(lRUCache.get(Mockito.<Long>any())).thenThrow(new BadRequestException("An error occurred"));
        CountryDTO country = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        // Act and Assert
        assertThrows(BadRequestException.class, () -> countryServiceImpl.updateWithContinent(country, continent));
        verify(lRUCache).get(eq(1L));
    }

    /**
     * Method under test:
     * {@link CountryServiceImpl#updateWithContinent(CountryDTO, Continent)}
     */
    @Test
    void testUpdateWithContinent3() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        ArrayList<Country> countries = new ArrayList<>();
        continent.setCountries(countries);
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");
        when(countryRepository.save(Mockito.<Country>any())).thenReturn(country);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        Country country2 = mock(Country.class);
        when(country2.getId()).thenReturn(1L);
        doNothing().when(country2).setContinent(Mockito.<Continent>any());
        doNothing().when(country2).setId(Mockito.<Long>any());
        doNothing().when(country2).setLatitude(Mockito.<Double>any());
        doNothing().when(country2).setLongitude(Mockito.<Double>any());
        doNothing().when(country2).setName(Mockito.<String>any());
        country2.setContinent(continent2);
        country2.setId(1L);
        country2.setLatitude(10.0d);
        country2.setLongitude(10.0d);
        country2.setName("Name");
        Optional<Country> ofResult = Optional.of(country2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Country>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        CountryDTO country3 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        Continent continent3 = new Continent();
        continent3.setCountries(new ArrayList<>());
        continent3.setId(1L);
        continent3.setLanguages(new HashSet<>());
        continent3.setName("Name");

        // Act
        countryServiceImpl.updateWithContinent(country3, continent3);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Country.class));
        verify(lRUCache).remove(eq(1L));
        verify(country2, atLeast(1)).getId();
        verify(country2, atLeast(1)).setContinent(isA(Continent.class));
        verify(country2).setId(eq(1L));
        verify(country2, atLeast(1)).setLatitude(eq(10.0d));
        verify(country2, atLeast(1)).setLongitude(eq(10.0d));
        verify(country2, atLeast(1)).setName(eq("Name"));
        verify(countryRepository).save(isA(Country.class));
        assertEquals("Name", country3.getName());
        assertEquals("Name", continent3.getName());
        assertEquals(10.0d, country3.getLatitude().doubleValue());
        assertEquals(10.0d, country3.getLongitude().doubleValue());
        assertEquals(1L, country3.getContinent().longValue());
        assertEquals(1L, country3.getId().longValue());
        assertEquals(1L, continent3.getId().longValue());
        assertTrue(continent3.getLanguages().isEmpty());
        assertEquals(countries, continent3.getCountries());
    }

    /**
     * Method under test: {@link CountryServiceImpl#update(Country)}
     */
    @Test
    void testUpdate() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");
        when(countryRepository.save(Mockito.<Country>any())).thenReturn(country);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Country>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        Country country2 = new Country();
        country2.setContinent(continent2);
        country2.setId(1L);
        country2.setLatitude(10.0d);
        country2.setLongitude(10.0d);
        country2.setName("Name");

        // Act
        countryServiceImpl.update(country2);

        // Assert that nothing has changed
        verify(lRUCache).put(eq(1L), isA(Country.class));
        verify(lRUCache).remove(eq(1L));
        verify(countryRepository).save(isA(Country.class));
        assertEquals("Name", country2.getName());
        assertEquals(10.0d, country2.getLatitude().doubleValue());
        assertEquals(10.0d, country2.getLongitude().doubleValue());
        assertEquals(1L, country2.getId().longValue());
        assertEquals(continent, country2.getContinent());
    }

    /**
     * Method under test: {@link CountryServiceImpl#update(Country)}
     */
    @Test
    void testUpdate2() throws ResourceNotFoundException {
        // Arrange
        doThrow(new BadRequestException("An error occurred")).when(lRUCache).remove(Mockito.<Long>any());

        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");

        // Act and Assert
        assertThrows(BadRequestException.class, () -> countryServiceImpl.update(country));
        verify(lRUCache).remove(eq(1L));
    }

    /**
     * Method under test: {@link CountryServiceImpl#update(Country)}
     */
    @Test
    void testUpdate3() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");
        when(countryRepository.save(Mockito.<Country>any())).thenReturn(country);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Country>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        Country country2 = mock(Country.class);
        when(country2.getId()).thenReturn(1L);
        doNothing().when(country2).setContinent(Mockito.<Continent>any());
        doNothing().when(country2).setId(Mockito.<Long>any());
        doNothing().when(country2).setLatitude(Mockito.<Double>any());
        doNothing().when(country2).setLongitude(Mockito.<Double>any());
        doNothing().when(country2).setName(Mockito.<String>any());
        country2.setContinent(continent2);
        country2.setId(1L);
        country2.setLatitude(10.0d);
        country2.setLongitude(10.0d);
        country2.setName("Name");

        // Act
        countryServiceImpl.update(country2);

        // Assert that nothing has changed
        verify(lRUCache).put(eq(1L), isA(Country.class));
        verify(lRUCache).remove(eq(1L));
        verify(country2, atLeast(1)).getId();
        verify(country2).setContinent(isA(Continent.class));
        verify(country2).setId(eq(1L));
        verify(country2).setLatitude(eq(10.0d));
        verify(country2).setLongitude(eq(10.0d));
        verify(country2).setName(eq("Name"));
        verify(countryRepository).save(isA(Country.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#update(CountryDTO)}
     */
    @Test
    void testUpdate4() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");
        when(countryRepository.save(Mockito.<Country>any())).thenReturn(country);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");

        Country country2 = new Country();
        country2.setContinent(continent2);
        country2.setId(1L);
        country2.setLatitude(10.0d);
        country2.setLongitude(10.0d);
        country2.setName("Name");
        Optional<Country> ofResult = Optional.of(country2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Country>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        CountryDTO country3 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act
        countryServiceImpl.update(country3);

        // Assert
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Country.class));
        verify(lRUCache).remove(eq(1L));
        verify(countryRepository).save(isA(Country.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#update(CountryDTO)}
     */
    @Test
    void testUpdate5() throws ResourceNotFoundException {
        // Arrange
        when(lRUCache.get(Mockito.<Long>any())).thenThrow(new BadRequestException("An error occurred"));
        CountryDTO country = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertThrows(BadRequestException.class, () -> countryServiceImpl.update(country));
        verify(lRUCache).get(eq(1L));
    }

    /**
     * Method under test: {@link CountryServiceImpl#update(CountryDTO)}
     */
    @Test
    void testUpdate6() throws ResourceNotFoundException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");

        Country country = new Country();
        country.setContinent(continent);
        country.setId(1L);
        country.setLatitude(10.0d);
        country.setLongitude(10.0d);
        country.setName("Name");
        when(countryRepository.save(Mockito.<Country>any())).thenReturn(country);

        Continent continent2 = new Continent();
        continent2.setCountries(new ArrayList<>());
        continent2.setId(1L);
        continent2.setLanguages(new HashSet<>());
        continent2.setName("Name");
        Country country2 = mock(Country.class);
        when(country2.getId()).thenReturn(1L);
        doNothing().when(country2).setContinent(Mockito.<Continent>any());
        doNothing().when(country2).setId(Mockito.<Long>any());
        doNothing().when(country2).setLatitude(Mockito.<Double>any());
        doNothing().when(country2).setLongitude(Mockito.<Double>any());
        doNothing().when(country2).setName(Mockito.<String>any());
        country2.setContinent(continent2);
        country2.setId(1L);
        country2.setLatitude(10.0d);
        country2.setLongitude(10.0d);
        country2.setName("Name");
        Optional<Country> ofResult = Optional.of(country2);
        when(lRUCache.get(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(lRUCache).put(Mockito.<Long>any(), Mockito.<Country>any());
        doNothing().when(lRUCache).remove(Mockito.<Long>any());
        CountryDTO country3 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act
        countryServiceImpl.update(country3);

        // Assert that nothing has changed
        verify(lRUCache).get(eq(1L));
        verify(lRUCache).put(eq(1L), isA(Country.class));
        verify(lRUCache).remove(eq(1L));
        verify(country2, atLeast(1)).getId();
        verify(country2).setContinent(isA(Continent.class));
        verify(country2).setId(eq(1L));
        verify(country2, atLeast(1)).setLatitude(eq(10.0d));
        verify(country2, atLeast(1)).setLongitude(eq(10.0d));
        verify(country2, atLeast(1)).setName(eq("Name"));
        verify(countryRepository).save(isA(Country.class));
        assertEquals("Name", country3.getName());
        assertEquals(10.0d, country3.getLatitude().doubleValue());
        assertEquals(10.0d, country3.getLongitude().doubleValue());
        assertEquals(1L, country3.getContinent().longValue());
        assertEquals(1L, country3.getId().longValue());
    }

    /**
     * Method under test: {@link CountryServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk() throws BadRequestException, DataAccessException {
        // Arrange
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        // Act
        countryServiceImpl.createBulk(new ArrayList<>());

        // Assert that nothing has changed
        verify(jdbcTemplate).batchUpdate(
                eq("INSERT into country (name, id, latitude, longitude, continent)VALUES (?, ?, ?, ?, ?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk2() throws BadRequestException, DataAccessException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ArrayList<CountryDTO> list = new ArrayList<>();
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        list.add(buildResult);

        // Act
        countryServiceImpl.createBulk(list);

        // Assert
        verify(continentRepository).getContinentById(eq(1L));
        verify(jdbcTemplate).batchUpdate(
                eq("INSERT into country (name, id, latitude, longitude, continent)VALUES (?, ?, ?, ?, ?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk3() throws BadRequestException, DataAccessException {
        // Arrange
        Optional<Continent> emptyResult = Optional.empty();
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(emptyResult);
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ArrayList<CountryDTO> list = new ArrayList<>();
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        list.add(buildResult);

        // Act
        countryServiceImpl.createBulk(list);

        // Assert
        verify(continentRepository).getContinentById(eq(1L));
        verify(jdbcTemplate).batchUpdate(
                eq("INSERT into country (name, id, latitude, longitude, continent)VALUES (?, ?, ?, ?, ?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk4() throws BadRequestException, DataAccessException {
        // Arrange
        Continent continent = new Continent();
        continent.setCountries(new ArrayList<>());
        continent.setId(1L);
        continent.setLanguages(new HashSet<>());
        continent.setName("Name");
        Optional<Continent> ofResult = Optional.of(continent);
        when(continentRepository.getContinentById(Mockito.<Long>any())).thenReturn(ofResult);
        when(jdbcTemplate.batchUpdate(Mockito.<String>any(), Mockito.<BatchPreparedStatementSetter>any()))
                .thenReturn(new int[]{1, -1, 1, -1});

        ArrayList<CountryDTO> list = new ArrayList<>();
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        list.add(buildResult);
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        list.add(buildResult2);

        // Act
        countryServiceImpl.createBulk(list);

        // Assert
        verify(continentRepository, atLeast(1)).getContinentById(eq(1L));
        verify(jdbcTemplate).batchUpdate(
                eq("INSERT into country (name, id, latitude, longitude, continent)VALUES (?, ?, ?, ?, ?)"),
                isA(BatchPreparedStatementSetter.class));
    }

    /**
     * Method under test: {@link CountryServiceImpl#createBulk(List)}
     */
    @Test
    void testCreateBulk5() throws BadRequestException {
        // Arrange
        when(continentRepository.getContinentById(Mockito.<Long>any()))
                .thenThrow(new BadRequestException("An error occurred"));

        ArrayList<CountryDTO> list = new ArrayList<>();
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        list.add(buildResult);

        // Act and Assert
        assertThrows(BadRequestException.class, () -> countryServiceImpl.createBulk(list));
        verify(continentRepository).getContinentById(eq(1L));
    }

    /**
     * Method under test:
     * {@link CountryServiceImpl#getBetweenLatitudes(Double, Double)}
     */
    @Test
    void testGetBetweenLatitudes() {
        // Arrange
        ArrayList<Country> countryList = new ArrayList<>();
        when(countryRepository.findAllCountryWithLatitudeBetween(Mockito.<Double>any(), Mockito.<Double>any()))
                .thenReturn(countryList);

        // Act
        List<Country> actualBetweenLatitudes = countryServiceImpl.getBetweenLatitudes(10.0d, 10.0d);

        // Assert
        verify(countryRepository).findAllCountryWithLatitudeBetween(eq(10.0d), eq(10.0d));
        assertTrue(actualBetweenLatitudes.isEmpty());
        assertSame(countryList, actualBetweenLatitudes);
    }

    /**
     * Method under test:
     * {@link CountryServiceImpl#getBetweenLatitudes(Double, Double)}
     */
    @Test
    void testGetBetweenLatitudes2() {
        // Arrange
        when(countryRepository.findAllCountryWithLatitudeBetween(Mockito.<Double>any(), Mockito.<Double>any()))
                .thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> countryServiceImpl.getBetweenLatitudes(10.0d, 10.0d));
        verify(countryRepository).findAllCountryWithLatitudeBetween(eq(10.0d), eq(10.0d));
    }
}
