package org.example.distancedata.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testExceptionMessage() {
        String expectedMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected value.");
    }

    @Test
    public void testThrowingResourceNotFoundException() {
        // Simulate a scenario where ResourceNotFoundException would be thrown
        ResourceNotFoundException thrownException = assertThrows(
                ResourceNotFoundException.class,
                () -> { throw new ResourceNotFoundException("Resource with given ID not found"); }
        );

        assertEquals("Resource with given ID not found", thrownException.getMessage(), "The exception should carry the expected error message.");
    }


}
