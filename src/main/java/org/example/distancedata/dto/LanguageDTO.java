package org.example.distancedata.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageDTO {
    private String name;
    private Long id;
}