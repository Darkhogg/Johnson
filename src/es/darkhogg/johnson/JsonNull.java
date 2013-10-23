package es.darkhogg.johnson;

/**
 * The class that represents the JSON <tt>null</tt> value. This class is implemented as a <i>singleton</i>, being
 * {@link #NULL} the only instance of this class.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonNull extends JsonValue<Void> {

    /** Singleton instance of this class */
    public static final JsonNull NULL = new JsonNull();

    private JsonNull () {
        super(null);
    }

    @Override
    public int hashCode () {
        return 0;
    }
    
    @Override
    public boolean equals (Object obj) {
        return (obj instanceof JsonNull);
    }
}
