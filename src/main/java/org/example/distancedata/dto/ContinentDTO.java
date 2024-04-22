package org.example.distancedata.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContinentDTO {
    private Long id;
    private String name;
    private List<String> languages;
}