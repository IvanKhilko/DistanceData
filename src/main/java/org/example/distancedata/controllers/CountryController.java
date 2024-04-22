package org.example.distancedata.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.distancedata.aspect.AspectAnnotation;
import org.example.distancedata.dto.CountryDTO;
import org.example.distancedata.entity.Country;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.services.DistanceService;
import org.example.distancedata.services.implementation.CountryServiceImpl;
import org.example.distancedata.services.implementation.ContinentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CountryController")
@RestController
@RequestMapping("/api/countries")
@AllArgsConstructor
public class CountryController {
    private final CountryServiceImpl dataService;
    private final DistanceService distanceService;
    private final ContinentServiceImpl continentService;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Country>> getAllCountry() {
        return new ResponseEntity<>(dataService.read(), HttpStatus.OK);
    }

    @AspectAnnotation
    @GetMapping(value = "/info", produces = "application/json")
    public ResponseEntity<Country> getCountryInfo(
            final @RequestParam(name = "country") String countryName)
            throws ResourceNotFoundException {
        var countryInfo = dataService.getByName(countryName);
        return new ResponseEntity<>(countryInfo, HttpStatus.OK);
    }

    @AspectAnnotation
    @GetMapping(value = "/find", produces = "application/json")
    public ResponseEntity<Country> getCountryInfoById(
            final @RequestParam(name = "id") Long id)
            throws ResourceNotFoundException {
        var countryInfo = dataService.getByID(id);
        return new ResponseEntity<>(countryInfo, HttpStatus.OK);
    }

    @AspectAnnotation
    @GetMapping(value = "/distance/{firstCountry}+{secondCountry}",
            produces = "application/json")
    public ResponseEntity<HashMap<String, String>> getDistance(
            final @PathVariable(name = "firstCountry") String firstCountry,
            final @PathVariable(name = "secondCountry") String secondCountry)
            throws ResourceNotFoundException {
        var firstCountryInfo = dataService.getByName(firstCountry);
        var secondCountryInfo = dataService.getByName(secondCountry);
        double distance = distanceService.getDistanceInKilometres(firstCountryInfo, secondCountryInfo);
        if (distance != -1) {
            var objects = new HashMap<String, String>();
            objects.put("First country info: ", firstCountryInfo.getName() + " "
                    + firstCountryInfo.getLongitude() + " " + firstCountryInfo.getLatitude());
            objects.put("Second country info: ", secondCountryInfo.getName() + " "
                    + secondCountryInfo.getLongitude() + " " + secondCountryInfo.getLatitude());
            objects.put("Distance", Double.toString(distance));
            return new ResponseEntity<>(objects, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @AspectAnnotation
    @PutMapping("/update")
    public HttpStatus update(final @RequestBody CountryDTO country)
            throws ResourceNotFoundException {
        dataService.update(country);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @PutMapping("/update/{continentName}")
    public HttpStatus update(final @RequestBody CountryDTO country,
                             final @PathVariable(name = "continentName")
                             String continentName)
            throws ResourceNotFoundException {
        var continent = continentService.getByName(continentName);
        dataService.updateWithContinent(country, continent);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @PostMapping("/create/{continentName}")
    public HttpStatus create(final @RequestBody CountryDTO country,
                             final @PathVariable(name = "continentName")
                             String continentName)
            throws ResourceNotFoundException, BadRequestException {
        var continent = continentService.getByName(continentName);
        dataService.createWithContinent(country, continent);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @DeleteMapping("/delete")
    public HttpStatus delete(final @RequestParam(name = "id") Long id)
            throws ResourceNotFoundException {
        dataService.delete(id);
        return HttpStatus.OK;
    }

    @AspectAnnotation
    @GetMapping("/getBetweenLatitude")
    public ResponseEntity<List<Country>> getCountriesBetween(
            final @RequestParam(name = "first") Double first,
            final @RequestParam(name = "second") Double second) {
        if (first > second) {
            return new ResponseEntity<>(
                    dataService.getBetweenLatitudes(second, first),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                dataService.getBetweenLatitudes(first, second), HttpStatus.OK);
    }

    @AspectAnnotation
    @PostMapping("/bulkCreate")
    public HttpStatus bulkCreate(
            @RequestBody final List<CountryDTO> countryDTOS) {
        dataService.createBulk(countryDTOS);
        return HttpStatus.OK;
    }
}