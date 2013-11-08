package es.darkhogg.johnson.data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Representation of a JSON number.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonNumber extends JsonValue<Number> {

    /**
     * Creates a JSON number with the given value.
     * 
     * @param value Value for the new JSON number
     * @throws NullPointerException if <tt>value</tt> is <tt>null</tt>
     * @throws IllegalArgumentException if <tt>value</tt> is not of an accepted <tt>Number</tt> subclass
     */
    private JsonNumber (Number value) {
        super(value);

        if (value == null) {
            throw new NullPointerException("value");
        }
        if (!(value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long
              || value instanceof Float || value instanceof Double || value instanceof BigInteger || value instanceof BigDecimal))
        {
            throw new IllegalArgumentException("value instanceof " + value.getClass().getName());
        }
    }

    @Override
    public int hashCode () {
        return value.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JsonNumber)) {
            return false;
        }

        JsonNumber jnum = (JsonNumber) obj;
        return value.equals(jnum.value);
    }

    public static JsonNumber valueOf (Number num) {
        return new JsonNumber(num);
    }

    public static JsonNumber valueOf (int num) {
        return JsonNumber.valueOf(Integer.valueOf(num));
    }

    public static JsonNumber valueOf (long num) {
        return JsonNumber.valueOf(Long.valueOf(num));
    }

    public static JsonNumber valueOf (float num) {
        return JsonNumber.valueOf(Float.valueOf(num));
    }

    public static JsonNumber valueOf (double num) {
        return JsonNumber.valueOf(Double.valueOf(num));
    }

    public static JsonNumber valueOf (String str) {
        if (str.indexOf('.') >= 0) {
            return JsonNumber.valueOf(new BigDecimal(str));
        } else {
            return JsonNumber.valueOf(new BigInteger(str));
        }
    }

}
