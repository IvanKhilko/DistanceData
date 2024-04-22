package org.example.distancedata.service_implementation;

import org.example.distancedata.entity.Country;
import org.example.distancedata.services.implementation.DistanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DistanceServiceImplTest {

    @InjectMocks
    private DistanceServiceImpl service;

    @Test
    void shouldCalculateDistance() {
        var firstCity = Country.builder()
                .name("Belarus")
                .longitude(23.0)
                .latitude(45.0)
                .build();
        var secondCity = Country.builder()
                .name("Russia")
                .longitude(123.0)
                .latitude(34.0)
                .build();
        assertEquals(4416.0342,
                service.getDistanceInKilometres(firstCity, secondCity));
    }
}