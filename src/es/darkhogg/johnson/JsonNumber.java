package es.darkhogg.johnson;

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
     */
    public JsonNumber (Number value) {
        super(value);

        if (value == null) {
            throw new NullPointerException("value");
        }
    }

}
