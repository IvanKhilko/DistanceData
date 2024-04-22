package org.example.distancedata.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryDTO {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Long continent;

}