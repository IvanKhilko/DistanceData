package org.example.distancedata.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.distancedata.dto.CountryDTO;
import org.example.distancedata.entity.Country;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.services.DistanceService;
import org.example.distancedata.services.implementation.CountryServiceImpl;
import org.example.distancedata.services.implementation.ContinentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryControllerTest {
    @Mock
    private CountryServiceImpl countryService;
    @Mock
    private DistanceService distanceService;
    @Mock
    private ContinentServiceImpl continentService;
    @InjectMocks
    private CountryController countryController;

    @Test
    void shouldReturnOkAll() {
        ResponseEntity<List<Country>> responseEntity = countryController.getAllCountry();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldFindById() throws ResourceNotFoundException {
        Long id = 1L;
        var mockCountry = new Country();
        when(countryService.getByID(id))
                .thenReturn(mockCountry);
        ResponseEntity<Country> responseEntity = countryController
                .getCountryInfoById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldNotFindById() throws ResourceNotFoundException {
        Long id = 1L;
        when(countryService.getByID(id))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class,
                () -> countryController.getCountryInfoById(id));
    }

    @Test
    void shouldFindByInfo() throws ResourceNotFoundException {
        String name = "Belarus";
        var mockCountry = new Country();
        when(countryService.getByName(name))
                .thenReturn(mockCountry);
        ResponseEntity<Country> responseEntity = countryController
                .getCountryInfo(name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldNotFindByInfo() throws ResourceNotFoundException {
        String name = "Belarus";
        when(countryService.getByName(name))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> countryController.getCountryInfo(name));
    }

    @Test
    void shouldAddCountry()
            throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Belarus")
                .latitude(23.0)
                .longitude(45.0)
                .build();
        String continentName = "Europe";
        Continent continent = new Continent();
        Country createdCountry = new Country();
        when(countryService.createWithContinent(countryDTO, continent)).thenReturn(createdCountry);
        when(continentService.getByName(continentName)).thenReturn(continent);
        var responseEntity = countryController.create(countryDTO, continentName);
        assertEquals(HttpStatus.OK, responseEntity);
    }
    @Test
    void shouldNotAddCountry() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Belarus")
                .latitude(23.0)
                .longitude(45.0)
                .build();
        String continentName = "Europe";
        Continent continent = new Continent();
        when(countryService.createWithContinent(countryDTO, continent)).thenThrow(BadRequestException.class);
        when(continentService.getByName(continentName)).thenReturn(continent);
        assertThrows(BadRequestException.class, () -> countryController.create(countryDTO, continentName));
    }
    @Test
    void shouldNotAddCountryNotExistedCountry() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Belarus")
                .latitude(23.0)
                .longitude(45.0)
                .build();
        String continentName = "Europe";
        when(continentService.getByName(continentName)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> countryController.create(countryDTO, continentName));
    }

    @Test
    void shouldDeleteWithId() throws ResourceNotFoundException {
        Long id = 1L;
        HttpStatus httpStatus = countryController.delete(id);
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    void shouldNotDeleteWithId() throws ResourceNotFoundException {
        Long id = 1L;
        doThrow(ResourceNotFoundException.class).when(countryService)
                .delete(id);
        assertThrows(ResourceNotFoundException.class, () -> countryController.delete(id));
    }

    @Test
    void shouldUpdateWithContinent() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Belarus")
                .latitude(23.0)
                .longitude(45.0)
                .id(3L)
                .build();
        String continentName = "Europe";
        Continent continent = Continent.builder().name(continentName).build();
        when(continentService.getByName(continentName)).thenReturn(continent);
        Mockito.doNothing().when(countryService).updateWithContinent(countryDTO, continent);
        var httpStatus = countryController.update(countryDTO, continentName);
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    void shouldUpdateWithNotExistedContinent() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Belarus")
                .latitude(23.0)
                .longitude(45.0)
                .id(3L)
                .build();
        String continentName = "Europe";
        when(continentService.getByName(continentName)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> countryController.update(countryDTO, continentName));
    }

    @Test
    void shouldNotUpdateWithContinent() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .name("Belarus")
                .latitude(23.0)
                .longitude(45.0)
                .id(3L)
                .build();
        String continentName = "Europe";
        Continent continent = Continent.builder().name(continentName).build();
        when(continentService.getByName(continentName)).thenReturn(continent);
        doThrow(ResourceNotFoundException.class).when(countryService).updateWithContinent(countryDTO, continent);
        assertThrows(ResourceNotFoundException.class, () -> countryController.update(countryDTO, continentName));
    }

    @Test
    void shouldUpdate() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .id(3L)
                .name("Belaruss")
                .build();
        HttpStatus httpStatus = countryController.update(countryDTO);
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    void shouldNotUpdate() throws ResourceNotFoundException {
        CountryDTO countryDTO = CountryDTO.builder()
                .id(3L)
                .name("Belaruss")
                .build();
        doThrow(ResourceNotFoundException.class).when(countryService).update(countryDTO);
        assertThrows(ResourceNotFoundException.class, () -> countryController.update(countryDTO));
    }

    @Test
    void bulkInsert() {
        HttpStatus httpStatus = countryController
                .bulkCreate(new ArrayList<>());
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    void shouldCalculateDistance() throws ResourceNotFoundException {
        var firstCountryInfo = new Country();
        var secondCountryInfo = new Country();
        when(countryService.getByName("Russia")).thenReturn(firstCountryInfo);
        when(countryService.getByName("Belarus")).thenReturn(secondCountryInfo);
        when(distanceService.getDistanceInKilometres(firstCountryInfo,
                secondCountryInfo)).thenReturn(4416.0342);
        var responseEntity = countryController.getDistance("Russia", "Belarus");
        assertEquals("4416.0342", Objects.requireNonNull(responseEntity.getBody()).get("Distance"));
    }

    @Test
    void shouldNotCalculateDistance() throws ResourceNotFoundException {
        when(countryService.getByName(anyString()))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class
                , () -> countryController.getDistance("Russia", "Belarus"));
    }

    @Test
    void shouldFindCountriesBetweenLatitudes() {
        Double first = 23D;
        Double second = 20D;
        countryController.getCountriesBetween(first, second);
        verify(countryService, times(1))
                .getBetweenLatitudes(first, second);
    }

}