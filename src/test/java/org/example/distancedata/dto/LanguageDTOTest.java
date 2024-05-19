package org.example.distancedata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LanguageDTOTest {

    @Test
    public void testLanguageDTOInitialization() {
        LanguageDTO languageDTO = LanguageDTO.builder()
                .id(1L)
                .name("English")
                .build();

        assertNotNull(languageDTO);
        assertEquals(1L, languageDTO.getId());
        assertEquals("English", languageDTO.getName());
    }

    @Test
    public void testLanguageDTOEquality() {
        LanguageDTO languageDTO1 = LanguageDTO.builder()
                .id(1L)
                .name("English")
                .build();

        LanguageDTO languageDTO2 = LanguageDTO.builder()
                .id(1L)
                .name("English")
                .build();

        assertEquals(languageDTO1, languageDTO2);
        assertEquals(languageDTO1.hashCode(), languageDTO2.hashCode());
    }

    @Test
    public void testLanguageDTOUpdate() {
        LanguageDTO languageDTO = LanguageDTO.builder()
                .id(1L)
                .name("English")
                .build();

        languageDTO.setName("Updated English");
        assertEquals("Updated English", languageDTO.getName());

        languageDTO.setId(2L);
        assertEquals(2L, languageDTO.getId());
    }
}
