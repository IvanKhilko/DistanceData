package org.example.distancedata.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ExceptionsHandler.class})
@ExtendWith(SpringExtension.class)
class ExceptionsHandlerDiffblueTest {
    @Autowired
    private ExceptionsHandler exceptionsHandler;

    /**
     * Method under test: {@link ExceptionsHandler#serverException(ServerException)}
     */
    @Test
    void testServerException() {
        // Arrange and Act
        ResponseEntity<ExceptionDetails> actualServerExceptionResult = exceptionsHandler
                .serverException(new ServerException("An error occurred"));

        // Assert
        assertEquals("An error occurred", actualServerExceptionResult.getBody().exceptionMessage());
        assertEquals(500, actualServerExceptionResult.getStatusCodeValue());
        assertTrue(actualServerExceptionResult.hasBody());
        assertTrue(actualServerExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link ExceptionsHandler#serverException(ServerException)}
     */
    @Test
    void testServerException2() {
        // Arrange
        ServerException exception = mock(ServerException.class);
        when(exception.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ExceptionDetails> actualServerExceptionResult = exceptionsHandler.serverException(exception);

        // Assert
        verify(exception).getMessage();
        assertEquals("Not all who wander are lost", actualServerExceptionResult.getBody().exceptionMessage());
        assertEquals(500, actualServerExceptionResult.getStatusCodeValue());
        assertTrue(actualServerExceptionResult.hasBody());
        assertTrue(actualServerExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link ExceptionsHandler#badRequestException(BadRequestException)}
     */
    @Test
    void testBadRequestException() {
        // Arrange and Act
        ResponseEntity<ExceptionDetails> actualBadRequestExceptionResult = exceptionsHandler
                .badRequestException(new BadRequestException("An error occurred"));

        // Assert
        assertEquals("An error occurred", actualBadRequestExceptionResult.getBody().exceptionMessage());
        assertEquals(400, actualBadRequestExceptionResult.getStatusCodeValue());
        assertTrue(actualBadRequestExceptionResult.hasBody());
        assertTrue(actualBadRequestExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link ExceptionsHandler#badRequestException(BadRequestException)}
     */
    @Test
    void testBadRequestException2() {
        // Arrange
        BadRequestException exception = mock(BadRequestException.class);
        when(exception.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ExceptionDetails> actualBadRequestExceptionResult = exceptionsHandler.badRequestException(exception);

        // Assert
        verify(exception).getMessage();
        assertEquals("Not all who wander are lost", actualBadRequestExceptionResult.getBody().exceptionMessage());
        assertEquals(400, actualBadRequestExceptionResult.getStatusCodeValue());
        assertTrue(actualBadRequestExceptionResult.hasBody());
        assertTrue(actualBadRequestExceptionResult.getHeaders().isEmpty());
    }
}
