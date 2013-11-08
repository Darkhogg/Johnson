package es.darkhogg.johnson.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import es.darkhogg.johnson.data.JsonNumber;

/**
 * Tests for the {@link JsonNumber} class.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonNumberTest {

    private Byte numByte;

    private Short numShort;

    private Integer numInt;

    private Long numLong;

    private Float numFloat;

    private Double numDouble;

    private BigInteger numBigInt;

    private BigDecimal numBigDecimal;

    @Before
    public void setUp () {
        numByte = Byte.valueOf((byte) 134);
        numShort = Short.valueOf((short) 2435);
        numInt = Integer.valueOf(10482047);
        numLong = Long.valueOf(0xCAFEBABEB00B1E5L);
        numFloat = Float.valueOf(0.1234f);
        numDouble = Double.valueOf(0.9876e+254);
        numBigInt = new BigInteger("1947628462956291834675639204756352836593");
        numBigDecimal = new BigDecimal("1.23e+12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void numberInstanceTest () {
        JsonNumber.valueOf(new AtomicInteger(0));
        Assert.fail();
    }

    @Test(expected = NullPointerException.class)
    public void notNullNumberTest () {
        JsonNumber.valueOf((Number) null);
        Assert.fail();
    }

    @Test(expected = NullPointerException.class)
    public void notNullStringTest () {
        JsonNumber.valueOf((String) null);
        Assert.fail();
    }

    @Test
    public void sameNumberTest () {
        Assert.assertEquals(numByte, JsonNumber.valueOf(numByte).getValue());
        Assert.assertEquals(numShort, JsonNumber.valueOf(numShort).getValue());
        Assert.assertEquals(numInt, JsonNumber.valueOf(numInt).getValue());
        Assert.assertEquals(numLong, JsonNumber.valueOf(numLong).getValue());
        Assert.assertEquals(numFloat, JsonNumber.valueOf(numFloat).getValue());
        Assert.assertEquals(numDouble, JsonNumber.valueOf(numDouble).getValue());
        Assert.assertEquals(numBigInt, JsonNumber.valueOf(numBigInt).getValue());
        Assert.assertEquals(numBigDecimal, JsonNumber.valueOf(numBigDecimal).getValue());
    }

    @Test
    public void samePrimitiveTest () {
        Assert.assertEquals(numByte.byteValue(), JsonNumber.valueOf(numByte.byteValue()).getValue().byteValue());
        Assert.assertEquals(numShort.shortValue(), JsonNumber.valueOf(numShort.shortValue()).getValue().shortValue());
        Assert.assertEquals(numInt.intValue(), JsonNumber.valueOf(numInt.intValue()).getValue().intValue());
        Assert.assertEquals(numLong.longValue(), JsonNumber.valueOf(numLong.longValue()).getValue().longValue());
        Assert.assertEquals(numFloat.floatValue(), JsonNumber.valueOf(numFloat.floatValue()).getValue().floatValue());
        Assert.assertEquals(numDouble.doubleValue(), JsonNumber.valueOf(numDouble.doubleValue()).getValue()
            .doubleValue());
    }

    @Test
    public void stringTest () {
        Assert.assertEquals(numBigInt, JsonNumber.valueOf(numBigInt.toString()).getValue());
        Assert.assertEquals(numBigDecimal, JsonNumber.valueOf(numBigDecimal.toString()).getValue());
    }
}
