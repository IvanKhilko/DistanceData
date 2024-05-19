package org.example.distancedata.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CountryDTOTest {
    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals() {
        // Arrange
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, null);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals2() {
        // Arrange
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, "Different type to CountryDTO");
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals3() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult = countryDTOBuilder.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals4() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.id(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder);
        CountryDTO buildResult = countryDTOBuilder2.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals5() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.id(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder);
        CountryDTO buildResult = countryDTOBuilder2.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals6() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.latitude(Mockito.<Double>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO buildResult = countryDTOBuilder3.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals7() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.latitude(Mockito.<Double>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO buildResult = countryDTOBuilder3.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(null)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals8() {
        // Arrange
        CountryDTO.CountryDTOBuilder builderResult = CountryDTO.builder();
        builderResult.id(1L);
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.latitude(Mockito.<Double>any())).thenReturn(builderResult);
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO buildResult = countryDTOBuilder3.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals9() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.longitude(Mockito.<Double>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO buildResult = countryDTOBuilder4.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(null)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals10() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.longitude(Mockito.<Double>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO buildResult = countryDTOBuilder4.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals11() {
        // Arrange
        CountryDTO.CountryDTOBuilder builderResult = CountryDTO.builder();
        builderResult.latitude(10.0d);
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.longitude(Mockito.<Double>any())).thenReturn(builderResult);
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO buildResult = countryDTOBuilder4.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(null)
                .latitude(null)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals12() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.name(Mockito.<String>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.longitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO.CountryDTOBuilder countryDTOBuilder5 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder5.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder4);
        CountryDTO buildResult = countryDTOBuilder5.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO.CountryDTOBuilder countryDTOBuilder6 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder6.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult2 = countryDTOBuilder6.continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals13() {
        // Arrange
        CountryDTO.CountryDTOBuilder builderResult = CountryDTO.builder();
        builderResult.longitude(10.0d);
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.name(Mockito.<String>any())).thenReturn(builderResult);
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.longitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO.CountryDTOBuilder countryDTOBuilder5 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder5.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder4);
        CountryDTO buildResult = countryDTOBuilder5.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO.CountryDTOBuilder countryDTOBuilder6 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder6.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult2 = countryDTOBuilder6.continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals14() {
        // Arrange
        CountryDTO.CountryDTOBuilder builderResult = CountryDTO.builder();
        builderResult.continent(1L);
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.name(Mockito.<String>any())).thenReturn(builderResult);
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.longitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO.CountryDTOBuilder countryDTOBuilder5 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder5.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder4);
        CountryDTO buildResult = countryDTOBuilder5.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO.CountryDTOBuilder countryDTOBuilder6 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder6.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult2 = countryDTOBuilder6.continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name("Name")
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Method under test: {@link CountryDTO#equals(Object)}
     */
    @Test
    void testEquals15() {
        // Arrange
        CountryDTO.CountryDTOBuilder builderResult = CountryDTO.builder();
        builderResult.name("Name");
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.name(Mockito.<String>any())).thenReturn(builderResult);
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.longitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO.CountryDTOBuilder countryDTOBuilder5 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder5.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder4);
        CountryDTO buildResult = countryDTOBuilder5.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO.CountryDTOBuilder countryDTOBuilder6 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder6.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult2 = countryDTOBuilder6.continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name(null)
                .build();

        // Act and Assert
        assertNotEquals(buildResult, buildResult2);
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CountryDTO#equals(Object)}
     *   <li>{@link CountryDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertEquals(buildResult, buildResult);
        int expectedHashCodeResult = buildResult.hashCode();
        assertEquals(expectedHashCodeResult, buildResult.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CountryDTO#equals(Object)}
     *   <li>{@link CountryDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode2() {
        // Arrange
        CountryDTO buildResult = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO buildResult2 = CountryDTO.builder()
                .continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();

        // Act and Assert
        assertEquals(buildResult, buildResult2);
        int expectedHashCodeResult = buildResult.hashCode();
        assertEquals(expectedHashCodeResult, buildResult2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CountryDTO#equals(Object)}
     *   <li>{@link CountryDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode3() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.longitude(Mockito.<Double>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO buildResult = countryDTOBuilder4.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO.CountryDTOBuilder countryDTOBuilder5 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder5.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult2 = countryDTOBuilder5.continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name("Name")
                .build();

        // Act and Assert
        assertEquals(buildResult, buildResult2);
        int expectedHashCodeResult = buildResult.hashCode();
        assertEquals(expectedHashCodeResult, buildResult2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CountryDTO#equals(Object)}
     *   <li>{@link CountryDTO#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode4() {
        // Arrange
        CountryDTO.CountryDTOBuilder countryDTOBuilder = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder.name(Mockito.<String>any())).thenReturn(CountryDTO.builder());
        CountryDTO.CountryDTOBuilder countryDTOBuilder2 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder2.longitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder);
        CountryDTO.CountryDTOBuilder countryDTOBuilder3 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder3.latitude(Mockito.<Double>any())).thenReturn(countryDTOBuilder2);
        CountryDTO.CountryDTOBuilder countryDTOBuilder4 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder4.id(Mockito.<Long>any())).thenReturn(countryDTOBuilder3);
        CountryDTO.CountryDTOBuilder countryDTOBuilder5 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder5.continent(Mockito.<Long>any())).thenReturn(countryDTOBuilder4);
        CountryDTO buildResult = countryDTOBuilder5.continent(1L)
                .id(1L)
                .latitude(10.0d)
                .longitude(10.0d)
                .name("Name")
                .build();
        CountryDTO.CountryDTOBuilder countryDTOBuilder6 = mock(CountryDTO.CountryDTOBuilder.class);
        when(countryDTOBuilder6.continent(Mockito.<Long>any())).thenReturn(CountryDTO.builder());
        CountryDTO buildResult2 = countryDTOBuilder6.continent(1L)
                .id(null)
                .latitude(null)
                .longitude(null)
                .name(null)
                .build();

        // Act and Assert
        assertEquals(buildResult, buildResult2);
        int expectedHashCodeResult = buildResult.hashCode();
        assertEquals(expectedHashCodeResult, buildResult2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CountryDTO#CountryDTO(Long, String, Double, Double, Long)}
     *   <li>{@link CountryDTO#setContinent(Long)}
     *   <li>{@link CountryDTO#setId(Long)}
     *   <li>{@link CountryDTO#setLatitude(Double)}
     *   <li>{@link CountryDTO#setLongitude(Double)}
     *   <li>{@link CountryDTO#setName(String)}
     *   <li>{@link CountryDTO#toString()}
     *   <li>{@link CountryDTO#getContinent()}
     *   <li>{@link CountryDTO#getId()}
     *   <li>{@link CountryDTO#getLatitude()}
     *   <li>{@link CountryDTO#getLongitude()}
     *   <li>{@link CountryDTO#getName()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        CountryDTO actualCountryDTO = new CountryDTO(1L, "Name", 10.0d, 10.0d, 1L);
        actualCountryDTO.setContinent(1L);
        actualCountryDTO.setId(1L);
        actualCountryDTO.setLatitude(10.0d);
        actualCountryDTO.setLongitude(10.0d);
        actualCountryDTO.setName("Name");
        String actualToStringResult = actualCountryDTO.toString();
        Long actualContinent = actualCountryDTO.getContinent();
        Long actualId = actualCountryDTO.getId();
        Double actualLatitude = actualCountryDTO.getLatitude();
        Double actualLongitude = actualCountryDTO.getLongitude();

        // Assert that nothing has changed
        assertEquals("CountryDTO(id=1, name=Name, latitude=10.0, longitude=10.0, continent=1)", actualToStringResult);
        assertEquals("Name", actualCountryDTO.getName());
        assertEquals(10.0d, actualLatitude.doubleValue());
        assertEquals(10.0d, actualLongitude.doubleValue());
        assertEquals(1L, actualContinent.longValue());
        assertEquals(1L, actualId.longValue());
    }
}
