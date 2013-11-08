package es.darkhogg.johnson.test;

import junit.framework.Assert;

import org.junit.Test;

import es.darkhogg.johnson.data.JsonString;

/**
 * Tests for the {@link JsonString} class.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public class JsonStringTest {

    /** Tests that a {@link JsonString} creation with a <tt>null</tt> causes a {@link NullPointerException}. */
    @Test(expected = NullPointerException.class)
    public void testNotNull () {
        new JsonString(null);
        Assert.fail();
    }

    /** Tests that {@link JsonString#EMPTY} has an empty value. */
    @Test
    public void testEmpty () {
        Assert.assertEquals("", JsonString.EMPTY.getValue());
    }

    /** Tests that {@link JsonString#equals} is correctly implemented. */
    @Test
    public void testEquals () {
        Assert.assertEquals(JsonString.EMPTY, new JsonString(""));

        String str = "Some characters to test equality";
        Assert.assertEquals(new JsonString(str), new JsonString(str));
        Assert.assertEquals(new JsonString(new String(str)), new JsonString(new String(str)));
    }

    /** Tests that {@link JsonString}s preserve their constructed values. */
    @Test
    public void testValue () {
        String str = "A string to test value equality";
        Assert.assertEquals(str, new JsonString(str).getValue());
    }
}
