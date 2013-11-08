package es.darkhogg.johnson.test;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Test;

import es.darkhogg.johnson.io.JsonWriter;

/**
 * Tests for the {@link JsonWriter} class.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public class JsonWriterTest {

    /** Tests that a writer cannot be constructed with a <tt>null</tt>. */
    @SuppressWarnings("resource")
    @Test(expected = NullPointerException.class)
    public void notNullTest () {
        new JsonWriter(null);
        Assert.fail();
    }

    /** Tests that a <tt>null</tt> is correctly written. */
    @SuppressWarnings("resource")
    @Test
    public void nullValueTest () throws IOException {
        StringWriter nullWriter = new StringWriter();

        new JsonWriter(nullWriter).valueNull();

        Assert.assertEquals("null", nullWriter.toString());
    }

    /** Tests that booleans are correctly written. */
    @SuppressWarnings("resource")
    @Test
    public void boolValueTest () throws IOException {
        StringWriter trueWriter = new StringWriter();
        StringWriter falseWriter = new StringWriter();

        new JsonWriter(trueWriter).value(true);
        new JsonWriter(falseWriter).value(false);

        Assert.assertEquals("true", trueWriter.toString());
        Assert.assertEquals("false", falseWriter.toString());
    }

    @Test
    public void intValueTest () throws IOException {
        StringWriter zeroWriter = new StringWriter();
        StringWriter positiveWriter = new StringWriter();
        StringWriter negativeWriter = new StringWriter();

        new JsonWriter(zeroWriter).value(0);
        new JsonWriter(positiveWriter).value(123);
        new JsonWriter(negativeWriter).value(-321);

        Assert.assertEquals("0", zeroWriter.toString());
        Assert.assertEquals("123", positiveWriter.toString());
        Assert.assertEquals("-321", negativeWriter.toString());
    }

    @Test
    public void longValueTest () throws IOException {
        StringWriter zeroWriter = new StringWriter();
        StringWriter positiveWriter = new StringWriter();
        StringWriter negativeWriter = new StringWriter();

        new JsonWriter(zeroWriter).value(0L);
        new JsonWriter(positiveWriter).value(12345678987654321L);
        new JsonWriter(negativeWriter).value(-98765432123456789L);

        Assert.assertEquals("0", zeroWriter.toString());
        Assert.assertEquals("12345678987654321", positiveWriter.toString());
        Assert.assertEquals("-98765432123456789", negativeWriter.toString());
    }

    @Test
    public void floatValueTest () throws IOException {
        StringWriter zeroWriter = new StringWriter();
        StringWriter positiveWriter = new StringWriter();
        StringWriter negativeWriter = new StringWriter();

        new JsonWriter(zeroWriter).value(0f);
        new JsonWriter(positiveWriter).value(123.123f);
        new JsonWriter(negativeWriter).value(-1.0e-38f);

        Assert.assertEquals("0.0", zeroWriter.toString());
        Assert.assertEquals("123.123", positiveWriter.toString());
        Assert.assertEquals("-1.0E-38", negativeWriter.toString());
    }

    @Test
    public void doubleValueTest () throws IOException {
        StringWriter zeroWriter = new StringWriter();
        StringWriter positiveWriter = new StringWriter();
        StringWriter negativeWriter = new StringWriter();

        new JsonWriter(zeroWriter).value(0.0);
        new JsonWriter(positiveWriter).value(1.23456789E127);
        new JsonWriter(negativeWriter).value(-9.87654321E-129);

        Assert.assertEquals("0.0", zeroWriter.toString());
        Assert.assertEquals("1.23456789E127", positiveWriter.toString());
        Assert.assertEquals("-9.87654321E-129", negativeWriter.toString());
    }

    @Test
    public void emptyObjectTest () throws IOException {
        StringWriter objWriter = new StringWriter();

        new JsonWriter(objWriter).beginObject().endObject();

        Assert.assertEquals("{}", objWriter.toString());
    }

    @Test
    public void emptyArrayTest () throws IOException {
        StringWriter arrWriter = new StringWriter();

        new JsonWriter(arrWriter).beginArray().endArray();

        Assert.assertEquals("[]", arrWriter.toString());
    }

    @Test
    public void nestingArrayTest () throws IOException {
        StringWriter arrWriter = new StringWriter();

        new JsonWriter(arrWriter).beginArray().beginArray().beginArray().endArray().beginArray().endArray().endArray()
            .beginArray().endArray().endArray();

        Assert.assertEquals("[[[],[]],[]]", arrWriter.toString());
    }
}
