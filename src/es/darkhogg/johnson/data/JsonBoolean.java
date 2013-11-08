package es.darkhogg.johnson.data;

/**
 * Representation of a JSON boolean.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonBoolean extends JsonValue<Boolean> {

    /** A JSON boolean with the <tt>false</tt> value */
    public static final JsonBoolean FALSE = new JsonBoolean(Boolean.FALSE);

    /** A JSON boolean with the <tt>true</tt> value */
    public static final JsonBoolean TRUE = new JsonBoolean(Boolean.TRUE);

    /**
     * Returns a JSON boolean with the specified boolean value.
     * 
     * @param value Value of the JSON boolean
     * @return A JSON boolean with the specified <tt>value</tt>
     */
    public static JsonBoolean valueOf (Boolean value) {
        return valueOf(value.booleanValue());
    }

    /**
     * Returns a JSON boolean with the specified boolean value.
     * 
     * @param value Value of the JSON boolean
     * @return A JSON boolean with the specified <tt>value</tt>
     */
    public static JsonBoolean valueOf (boolean value) {
        return value ? TRUE : FALSE;
    }

    private JsonBoolean (Boolean value) {
        super(value);
    }
}
