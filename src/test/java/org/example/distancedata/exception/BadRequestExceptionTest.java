package org.example.distancedata.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BadRequestExceptionTest {

    @Test
    public void testBadRequestExceptionWithMessage() {
        String errorMessage = "Invalid input";
        BadRequestException exception = new BadRequestException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testThrowingBadRequestException() {
        String errorMessage = "Invalid data provided";

        BadRequestException thrown = assertThrows(
                BadRequestException.class,
                () -> {
                    throw new BadRequestException(errorMessage);
                },
                "Expected BadRequestException to be thrown"
        );

        assertEquals(errorMessage, thrown.getMessage());
    }
}
