package org.example.distancedata.exception;

import org.example.distancedata.exception.ExceptionDetails;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionDetailsTest {

    @Test
    public void testCreateExceptionDetails() {
        Date date = new Date();
        String message = "Some exception message";

        ExceptionDetails details = new ExceptionDetails(date, message);

        assertEquals(date, details.date());
        assertEquals(message, details.exceptionMessage());
    }



    @Test
    public void testEqualsAndHashCode() {
        Date date = new Date();
        ExceptionDetails details1 = new ExceptionDetails(date, "Message 1");
        ExceptionDetails details2 = new ExceptionDetails(date, "Message 1");
        ExceptionDetails details3 = new ExceptionDetails(date, "Message 2");

        assertEquals(details1, details2); // They have the same fields
        assertNotEquals(details1, details3); // Different messages

        assertEquals(details1.hashCode(), details2.hashCode());
        assertNotEquals(details1.hashCode(), details3.hashCode());
    }

    @Test
    public void testToString() {
        Date date = new Date();
        ExceptionDetails details = new ExceptionDetails(date, "Test message");

        String expectedString = "ExceptionDetails[date=" + date + ", exceptionMessage=Test message]";
        assertEquals(expectedString, details.toString());
    }
}
