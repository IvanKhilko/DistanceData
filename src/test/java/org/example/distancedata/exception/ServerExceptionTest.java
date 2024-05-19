package org.example.distancedata.exception;

import org.example.distancedata.exception.ServerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerExceptionTest {

    @Test
    public void testExceptionMessage() {
        String errorMessage = "Server encountered an error";
        ServerException exception = new ServerException(errorMessage);
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the expected value.");
    }

    @Test
    public void testThrowingServerException() {
        // Simulate a scenario where ServerException would be thrown
        Exception exception = assertThrows(
                ServerException.class,
                () -> { throw new ServerException("Critical server error"); }
        );

        assertEquals("Critical server error", exception.getMessage(), "The exception should carry the expected error message.");
    }
}
