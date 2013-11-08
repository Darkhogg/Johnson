package es.darkhogg.johnson.test;

import junit.framework.Assert;

import org.junit.Test;

import es.darkhogg.johnson.data.JsonBoolean;

/**
 * Tests for the {@link JsonBoolean} class.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public class JsonBooleanTest {

    /** Tests that the {@link JsonBoolean#TRUE} object has a <tt>true</tt> value. */
    @Test
    public void testTrue () {
        Assert.assertTrue(JsonBoolean.TRUE.getValue().booleanValue());
    }

    /** Tests that the {@link JsonBoolean#FALSE} object has a <tt>false</tt> value. */
    @Test
    public void testFalse () {
        Assert.assertFalse(JsonBoolean.FALSE.getValue().booleanValue());
    }

    /**
     * Tests that a {@link JsonBoolean#valueOf(Boolean)} invocation with a <tt>null</tt> causes a
     * {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void testNotNull () {
        JsonBoolean.valueOf(null);
        Assert.fail();
    }

    /**
     * Tests that the different invocations of {@link JsonBoolean#valueOf} always return {@link JsonBoolean#TRUE} or
     * {@link JsonBoolean#FALSE} and not other references.
     */
    @Test
    public void testSingle () {
        Assert.assertSame(JsonBoolean.valueOf(false), JsonBoolean.FALSE);
        Assert.assertSame(JsonBoolean.valueOf(true), JsonBoolean.TRUE);

        Assert.assertSame(JsonBoolean.valueOf(Boolean.FALSE), JsonBoolean.FALSE);
        Assert.assertSame(JsonBoolean.valueOf(Boolean.TRUE), JsonBoolean.TRUE);

        Assert.assertSame(JsonBoolean.valueOf(new Boolean(false)), JsonBoolean.FALSE);
        Assert.assertSame(JsonBoolean.valueOf(new Boolean(true)), JsonBoolean.TRUE);
    }

}
