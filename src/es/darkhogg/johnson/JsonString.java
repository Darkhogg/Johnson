package es.darkhogg.johnson;

/**
 * Representation of a JSON number.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonString extends JsonValue<String> {

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

}
