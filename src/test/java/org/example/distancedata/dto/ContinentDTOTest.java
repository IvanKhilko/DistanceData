package org.example.distancedata.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContinentDTOTest {

    @Test
    public void testContinentDTOInitialization() {
        List<String> languages = List.of("English", "French");

        ContinentDTO continentDTO = ContinentDTO.builder()
                .id(1L)
                .name("Europe")
                .languages(languages)
                .build();

        assertNotNull(continentDTO);
        assertEquals(1L, continentDTO.getId());
        assertEquals("Europe", continentDTO.getName());
        assertEquals(languages, continentDTO.getLanguages());
    }

    @Test
    public void testContinentDTOEquality() {
        List<String> languages = List.of("English", "French");

        ContinentDTO continentDTO1 = ContinentDTO.builder()
                .id(1L)
                .name("Europe")
                .languages(languages)
                .build();

        ContinentDTO continentDTO2 = ContinentDTO.builder()
                .id(1L)
                .name("Europe")
                .languages(languages)
                .build();

        assertEquals(continentDTO1, continentDTO2);
        assertEquals(continentDTO1.hashCode(), continentDTO2.hashCode());
    }

    @Test
    public void testContinentDTOUpdate() {
        ContinentDTO continentDTO = ContinentDTO.builder()
                .id(1L)
                .name("Europe")
                .languages(List.of("English", "French"))
                .build();

        continentDTO.setName("Updated Europe");
        continentDTO.setLanguages(List.of("Spanish"));

        assertEquals("Updated Europe", continentDTO.getName());
        assertEquals(List.of("Spanish"), continentDTO.getLanguages());
    }
}
