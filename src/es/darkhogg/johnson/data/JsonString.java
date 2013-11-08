package es.darkhogg.johnson.data;

/**
 * Representation of a JSON number.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonString extends JsonValue<String> {

    /** The empty string. */
    public static final JsonString EMPTY = new JsonString("");

    /**
     * Creates a new JSON string with the given value.
     * 
     * @param value Value for this JSON string
     */
    public JsonString (String value) {
        super(value);

        if (value == null) {
            throw new NullPointerException("value");
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
        if (!(obj instanceof JsonString)) {
            return false;
        }
        
        JsonString jstr = (JsonString) obj;
        return value.equals(jstr.value);
    }
}
