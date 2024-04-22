package org.example.distancedata.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.distancedata.aspect.AspectAnnotation;
import org.example.distancedata.dto.ContinentDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.services.implementation.ContinentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ContinentController")
@RestController
@RequestMapping("/api/continents")
@AllArgsConstructor
public class ContinentController {
    private final ContinentServiceImpl continentService;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Continent>> getAll() {
        return new ResponseEntity<>(continentService.read(), HttpStatus.OK);
    }

    @AspectAnnotation
    @GetMapping(value = "/info", produces = "application/json")
    public ResponseEntity<Continent> getContinent(
            final @RequestParam(name = "continent") String name)
            throws ResourceNotFoundException {
        var continent = continentService.getByName(name);
        return new ResponseEntity<>(continent, HttpStatus.OK);
    }

    @AspectAnnotation
    @GetMapping(value = "/find", produces = "application/json")
    public ResponseEntity<Continent> getContinentById(
            final @RequestParam(name = "id") Long id)
            throws ResourceNotFoundException {
        var continent = continentService.getByID(id);
        return new ResponseEntity<>(continent, HttpStatus.OK);
    }

    @AspectAnnotation
    @PutMapping("/update")
    public HttpStatus update(final @RequestBody ContinentDTO continentDto)
            throws ResourceNotFoundException {
        continentService.update(continentDto);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @PostMapping("/create")
    public HttpStatus create(final @RequestBody ContinentDTO continentDto)
            throws BadRequestException {
        continentService.create(continentDto);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @PostMapping("/bulkCreate")
    public HttpStatus bulkCreate(
            @RequestBody final List<ContinentDTO> continents) {
        continentService.createBulk(continents);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @DeleteMapping("/delete")
    public HttpStatus delete(final @RequestParam(name = "id") Long id)
            throws ResourceNotFoundException {
        continentService.delete(id);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @PutMapping("/add_language")
    public HttpStatus addLanguages(
            final @RequestBody ContinentDTO continentDto)
            throws ResourceNotFoundException {
        continentService.modifyLanguage(continentDto, false);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @PutMapping("/delete_language")
    public HttpStatus deleteLanguages(
            final @RequestBody ContinentDTO continentDto)
            throws ResourceNotFoundException {
        continentService.modifyLanguage(continentDto, true);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @GetMapping("/get_by_language")
    public ResponseEntity<List<Continent>> getContinentsByLanguage(
            final @RequestParam(name = "id") Long id)
            throws ResourceNotFoundException {
        var continents = continentService.getByLanguage(id);
        return new ResponseEntity<>(continents, HttpStatus.OK);
    }

}