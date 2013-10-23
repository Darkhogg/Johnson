package es.darkhogg.johnson.test;

import junit.framework.Assert;

import org.junit.Test;

import es.darkhogg.johnson.JsonNull;

/**
 * Tests for the {@link JsonNull} class.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public class JsonNullTest {

    /** Tests that the value for {@link JsonNull#NULL} is <tt>null</tt> */
    @Test
    public void testValue () {
        Assert.assertNull(JsonNull.NULL.getValue());
    }

}
