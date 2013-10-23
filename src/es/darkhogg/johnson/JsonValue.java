package es.darkhogg.johnson;

/**
 * A class that represents a JSON value type.
 * 
 * @author Daniel Escoz
 * @version 1.0
 * @param <T> Type of the value this object is wrapping.
 */
public abstract class JsonValue<T> {
    
    /** Value wrapped in this JSON value */
    private final T value;
    
    /**
     * Constructs the JSON value with the given type and value.
     * 
     * @param value The value for this instance.
     */
    protected JsonValue (T value) {
        this.value = value;
    }
    
    /**
     * @return The actual value of this JSON value.
     */
    public final T getValue () {
        return value;
    }
    
}
