package org.example.distancedata.controllers;

import java.util.ArrayList;
import java.util.List;
import org.example.distancedata.dto.ContinentDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
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
public class ContinentControllerTest {
    @Mock
    private ContinentServiceImpl continentService;
    @InjectMocks
    private ContinentController continentController;

    @Test
    void shouldReturnAll() {
        ResponseEntity<List<Continent>> responseEntity = continentController.getAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldFindById() throws ResourceNotFoundException {
        Long id = 1L;
        var mockContinent = new Continent();
        when(continentService.getByID(id))
                .thenReturn(mockContinent);
        var responseEntity = continentController.getContinentById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void shouldNotFindById() throws ResourceNotFoundException {
        Long id = 1L;
        when(continentService.getByID(id))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> continentController.getContinentById(id));
    }

    @Test
    void shouldFindByName() throws ResourceNotFoundException {
        String name = "Europe";
        var mockContinent = new Continent();
        when(continentService.getByName(name))
                .thenReturn(mockContinent);
        var responseEntity = continentController.getContinent(name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void shouldNotFindByName() throws ResourceNotFoundException {
        String name = "Europe";
        when(continentService.getByName(name))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> continentController.getContinent(name));
    }

    @Test
    void shouldCreate() {
        ContinentDTO continentDTO = ContinentDTO.builder()
                .name("Hello").build();
        var status = continentController.create(continentDTO);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void shouldNotCreate()
            throws BadRequestException {
        var continent = ContinentDTO.builder()
                .name("Europe").build();
        Mockito.doThrow(BadRequestException.class).when(continentService).create(continent);
        assertThrows(BadRequestException.class, () -> continentController.create(continent));
    }

    @Test
    void shouldDeleteWithId() throws ResourceNotFoundException {
        Long id = 1L;
        HttpStatus httpStatus = continentController.delete(id);
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    void shouldNotDeleteWithId() throws ResourceNotFoundException {
        Long id = 1L;
        doThrow(ResourceNotFoundException.class).when(continentService).delete(id);
        assertThrows(ResourceNotFoundException.class,() -> continentController.delete(id));
    }


    @Test
    void shouldUpdateContinent() throws ResourceNotFoundException {
        var continent = ContinentDTO.builder()
                .id(3L)
                .name("Belaruss")
                .build();
        HttpStatus httpStatus = continentController.update(continent);
        assertEquals(HttpStatus.OK, httpStatus);
    }
    @Test
    void shouldNotUpdateContinent() throws ResourceNotFoundException {
        var continent = ContinentDTO.builder()
                .id(3L)
                .name("Belaruss")
                .build();
        doThrow(ResourceNotFoundException.class).when(continentService).update(continent);
        assertThrows(ResourceNotFoundException.class, () -> continentController.update(continent));
    }

    @Test
    void shouldAddLanguages()
            throws ResourceNotFoundException {
        var continent = ContinentDTO.builder().name("Europe")
                .languages(new ArrayList<>()).build();
        doNothing().when(continentService).modifyLanguage(continent, false);
        var responseEntity = continentController
                .addLanguages(continent);
        assertEquals(HttpStatus.OK, responseEntity);
    }
    @Test
    void shouldNotAddLanguages()
            throws ResourceNotFoundException {
        var continent = ContinentDTO.builder().name("Europe")
                .languages(new ArrayList<>()).build();
        doThrow(ResourceNotFoundException.class).when(continentService)
                .modifyLanguage(continent, true);
        assertThrows(ResourceNotFoundException.class, () ->continentController
                .deleteLanguages(continent));
    }

    @Test
    void shouldDeleteLanguages() throws ResourceNotFoundException {
        var continent= ContinentDTO.builder().name("Europe")
                .languages(new ArrayList<>()).build();
        doNothing().when(continentService).modifyLanguage(continent, true);
        var responseEntity = continentController
                .deleteLanguages(continent);
        assertEquals(HttpStatus.OK, responseEntity);
    }
    @Test
    void shouldNotDeleteLanguages() throws ResourceNotFoundException {
        var continent = ContinentDTO.builder().name("Europe")
                .languages(new ArrayList<>()).build();
        doThrow(ResourceNotFoundException.class).when(continentService)
                .modifyLanguage(continent, true);
        assertThrows(ResourceNotFoundException.class, () ->continentController
                .deleteLanguages(continent));
    }
    @Test
    void bulkInsert() {
        HttpStatus httpStatus = continentController.bulkCreate(new ArrayList<>());
        assertEquals(HttpStatus.OK, httpStatus);
    }
    @Test
    void getCountriesByLanguage() throws ResourceNotFoundException {
        Long id = 22L;
        when(continentService.getByLanguage(id))
                .thenReturn(new ArrayList<>());
        var responseEntity = continentController.getContinentsByLanguage(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    @Test
    void getCountriesByLanguageNotFindLanguage() throws ResourceNotFoundException {
        Long id = 22L;
        when(continentService.getByLanguage(id))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> continentController.getContinentsByLanguage(id));
    }
}