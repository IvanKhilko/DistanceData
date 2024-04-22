package org.example.distancedata.service_implementation;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.example.distancedata.services.implementation.CountryServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {
    @Mock
    private CountryRepository repository;
    @Mock
    private ContinentRepository continentRepository;
    @Mock
    private LRUCache<Long, Country> cache;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private CountryServiceImpl service;

    @Test
    public void returnAllCountry() {
        List<Country> expectedCountries = new ArrayList<>();
        when(repository.findAll(Sort.by("id")))
                .thenReturn(expectedCountries);
        List<Country> actualCountries = service.read();
        assertEquals(expectedCountries, actualCountries);
    }

    @Test
    public void createCountryWithContinentSuccess() {
        CountryDTO country = CountryDTO.builder()
                .name("Japan")
                .latitude(13.4543)
                .longitude(12.4445)
                .build();
        var createdCountry = service.createWithContinent(country, Continent.builder().name("Asia").id(1L).build());
        assertEquals(createdCountry.getName(), country.getName());
        assertEquals(createdCountry.getLatitude(), country.getLatitude());
        assertEquals(createdCountry.getLongitude(), country.getLongitude());
        assertEquals(createdCountry.getContinent().getName(), "Asia");
        verify(repository, times(1)).save(any(Country.class));
        verify(cache, times(1)).put(anyLong(), any(Country.class));
    }

    @Test
    public void createCountryWithCountryWrongName() {
        CountryDTO country = CountryDTO.builder()
                .name("Japan")
                .latitude(13.4543)
                .longitude(12.4445)
                .build();
        var continent = Continent.builder().name("Asia").id(1L).build();
        when(repository.getCountryByName("Japan")).thenReturn(Optional.of(new Country()));
        assertThrows(BadRequestException.class, () -> service.createWithContinent(country, continent));
    }

    @Test
    public void findCountryByIdNotInCache()
            throws ResourceNotFoundException {
        Long id = 5L;
        Optional<Country> expectedCountry = Optional.of(new Country());
        when(cache.get(id)).thenReturn(Optional.empty());
        when(repository.getCountryById(id)).thenReturn(expectedCountry);
        Country actualCountry = service.getByID(id);
        assertEquals(expectedCountry.get(), actualCountry);
        verify(cache, times(1))
                .put(id, expectedCountry.get());
    }

    @Test
    public void findCountryByIdNotExist() {
        Long id = 5L;
        when(cache.get(id)).thenReturn(Optional.empty());
        when(repository.getCountryById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByID(id));
        verify(cache, never())
                .put(anyLong(), any(Country.class));
    }

    @Test
    public void findCountryByIdInCache()
            throws ResourceNotFoundException {
        Long id = 5L;
        Optional<Country> expectedCountry = Optional.of(new Country());
        when(cache.get(id)).thenReturn(expectedCountry);
        var actualCountry = Optional.of(service.getByID(id));
        assertEquals(expectedCountry, actualCountry);
        verify(repository, never()).findById(anyLong());
    }

    @Test
    public void findCountryByNameNotInCache()
            throws ResourceNotFoundException {
        String name = "Japan";
        Optional<Country> expectedCountry = Optional.of(new Country());
        when(repository.getCountryByName(name)).thenReturn(expectedCountry);
        Country actualCountry = service.getByName(name);
        assertEquals(expectedCountry.get(), actualCountry);
        verify(cache, times(1)).put(expectedCountry.get().getId(), expectedCountry.get());
    }

    @Test
    public void findCountryByNameNotExist() {
        String name = "Japan";
        when(repository.getCountryByName(name)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByName(name));
        verify(cache, never()).put(anyLong(), any(Country.class));
    }

    @Test
    public void createCountry() {
        var newCountry = Country.builder()
                .name("Japan").build();
        var createdCountry = service.create(newCountry);
        assertEquals(createdCountry.getName(), newCountry.getName());
        verify(cache, times(1))
                .put(newCountry.getId(), newCountry);
        verify(repository, times(1)).save(newCountry);
    }

    @Test
    public void updateCountryWithContinent() throws ResourceNotFoundException {
        CountryDTO country = CountryDTO.builder()
                .name("Japann")
                .latitude(13.4543)
                .longitude(12.4445)
                .id(666L)
                .build();
        var expectedCountry = Country.builder()
                .name("Japan")
                .id(666L)
                .build();
        var continent = Continent.builder().name("Japan").id(1L).build();
        when(repository.getCountryById(country.getId()))
                .thenReturn(Optional.of(expectedCountry));
        service.updateWithContinent(country, continent);
        assertEquals(expectedCountry.getContinent().getName(), continent.getName());
        assertEquals(expectedCountry.getName(), country.getName());
        assertEquals(expectedCountry.getLatitude(), country.getLatitude());
        assertEquals(expectedCountry.getLongitude(), country.getLongitude());
        verify(cache, times(1)).remove(country.getId());
        verify(repository).save(expectedCountry);
    }

    @Test
    public void updateCountryWithCountryInvalidId() {
        var country = CountryDTO.builder()
                .name("Tokyko")
                .latitude(13.4543)
                .longitude(12.4445)
                .id(666L)
                .build();
        var continent = Continent.builder().name("Asia").id(1L).build();
        when(repository.getCountryById(country.getId()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateWithContinent(country, continent));
        verify(cache, never()).remove(country.getId());
        verify(cache, never())
                .put(anyLong(), any(Country.class));
        verify(repository, never()).save(any(Country.class));
    }

    @Test
    public void updateCountry() throws ResourceNotFoundException {
        var newCountry = CountryDTO.builder()
                .name("Japann")
                .latitude(13.4543)
                .longitude(12.4445)
                .id(666L)
                .build();
        var expectedCountry = Country.builder()
                .id(666L)
                .build();
        when(cache.get(newCountry.getId())).thenReturn(Optional.empty());
        when(repository.getCountryById(newCountry.getId()))
                .thenReturn(Optional.of(expectedCountry));
        service.update(newCountry);
        assertEquals(expectedCountry.getName(), newCountry.getName());
        assertEquals(expectedCountry.getLatitude(), newCountry.getLatitude());
        assertEquals(expectedCountry.getLongitude(), newCountry.getLongitude());
        verify(cache, times(1))
                .remove(newCountry.getId());
        verify(repository, times(1)).save(expectedCountry);
    }

    @Test
    public void updateCountryInCache() throws ResourceNotFoundException {
        CountryDTO newCountry = CountryDTO.builder()
                .name("Japann")
                .latitude(13.4543)
                .longitude(12.4445)
                .id(666L)
                .build();
        var expectedCountry = Country.builder()
                .id(666L)
                .build();
        when(cache.get(newCountry.getId())).thenReturn(Optional.of(expectedCountry));
        service.update(newCountry);
        assertEquals(expectedCountry.getName(), newCountry.getName());
        assertEquals(expectedCountry.getLatitude(), newCountry.getLatitude());
        assertEquals(expectedCountry.getLongitude(), newCountry.getLongitude());
        verify(cache, times(1))
                .remove(newCountry.getId());
        verify(repository, times(1)).save(expectedCountry);
    }

    @Test
    public void updateCountryInvalidId() {
        CountryDTO newCountry = CountryDTO.builder()
                .name("Japann")
                .latitude(13.4543)
                .longitude(12.4445)
                .id(666L)
                .build();
        when(cache.get(newCountry.getId())).thenReturn(Optional.empty());
        when(repository.getCountryById(newCountry.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(newCountry));

    }

    @Test
    public void shouldUpdateCountry() throws ResourceNotFoundException {
        var newCountry = Country.builder()
                .name("Japann")
                .latitude(13.4543)
                .longitude(12.4445)
                .id(666L)
                .build();
        service.update(newCountry);
        verify(cache, times(1))
                .remove(newCountry.getId());
        verify(cache, times(1))
                .put(anyLong(), any(Country.class));
        verify(repository, times(1))
                .save(newCountry);
    }

    @Test
    public void deleteCountryValidId() throws ResourceNotFoundException {
        Long id = 5L;
        when(repository.getCountryById(id)).thenReturn(Optional.of(new Country()));
        service.delete(id);
        verify(cache, times(1))
                .remove(id);
        verify(repository, times(1))
                .deleteById(id);
    }

    @Test
    public void deleteCountryInvalidId() {
        Long id = 5L;
        when(repository.getCountryById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
        verify(cache, never())
                .remove(id);
        verify(repository, never())
                .deleteById(id);
    }

    @Test
    public void bulkInsert() {
        CountryDTO firstCountry = CountryDTO.builder()
                .name("Belarus")
                .longitude(23.0)
                .latitude(45.0)
                .continent(2L)
                .build();
        CountryDTO secondCountry = CountryDTO.builder()
                .name("Russia")
                .longitude(123.0)
                .latitude(34.0)
                .continent(2L)
                .build();
        List<CountryDTO> countryDTOS = Arrays.asList(firstCountry, secondCountry);
        Optional<Continent> expectedContinent = Optional.of(new Continent());
        when(continentRepository.getContinentById(anyLong()))
                .thenReturn(expectedContinent);
        service.createBulk(countryDTOS);
        String sql = "INSERT into country (name, id, latitude, longitude, id_country)"
                + "VALUES (?, ?, ?, ?, ?)";
        verify(jdbcTemplate, times(1))
                .batchUpdate(eq(sql), any(BatchPreparedStatementSetter.class));

    }

    @Test
    public void countryBetweenLatitudes() {
        Double first = 10D;
        Double second = 20D;
        service.getBetweenLatitudes(first, second);
        verify(repository, times(1))
                .findAllCountryWithLatitudeBetween(first, second);
    }
}