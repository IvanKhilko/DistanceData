package org.example.distancedata.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class ContinentDTO {
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private String name;
    private List<String> languages;

}