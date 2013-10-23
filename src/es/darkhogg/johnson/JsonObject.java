package es.darkhogg.johnson;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a JSON object.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonObject extends JsonValue<Map<String, JsonValue<?>>> {

    /**
     * Creates a JSON object using the given map.
     * <p>
     * This method does not copy nor wraps the passed map in any way, so any callers <i>MUST</i> ensure that the map
     * remains unmodifiable by their own means.
     * 
     * @param value The map to be wrapped in this JSON object
     */
    private JsonObject (Map<String, JsonValue<?>> value) {
        super(value);
    }

    /**
     * Get the number of elements in this JSON object.
     * 
     * @return The size of this object
     */
    public int getSize () {
        return getValue().size();
    }

    /**
     * Return the element associated with the string indicated by <tt>key</tt>.
     * 
     * @param key Object key to return
     * @return The element associated with <tt>key</tt>
     */
    public JsonValue<?> get (String key) {
        return getValue().get(key);
    }

    /**
     * A class used to create JSON objects element by element.
     * 
     * @author Daniel Escoz
     * @version 1.0
     */
    public static final class Builder {
        /** Internal map used for the builder */
        private Map<String, JsonValue<?>> map;

        /** Whether the list is already used */
        private boolean dirty;

        /** Creates a new <tt>Builder</tt> with no elements. */
        public Builder () {
            dirty = true;
        }

        /**
         * Creates a new <tt>Builder</tt> with the elements of the given map.
         * 
         * @param elems Elements to add to this builder
         */
        public Builder (Map<String, JsonValue<?>> elems) {
            this();
            putAll(elems);
        }

        /**
         * Adds a mapping to this JSON object builder, or replaces an already added mapping based on the key.
         * 
         * @param key Key of the mapping
         * @param value Value of the mapping
         * @return <tt>this</tt>
         */
        public Builder put (String key, JsonValue<?> value) {
            prepare();
            map.put(key, value);
            return this;
        }

        /**
         * Adds all mappings from the given map to this JSON object builder, or replaces any existing mapping based
         * onthe key.
         * 
         * @param elems Mappings to add
         * @return <tt>this</tt>
         */
        public Builder putAll (Map<String, ? extends JsonValue<?>> elems) {
            prepare();
            map.putAll(elems);
            return this;
        }

        /** Prepares this builder. */
        private void prepare () {
            if (dirty) {
                if (map == null) {
                    map = new HashMap<String, JsonValue<?>>();
                } else {
                    map = new HashMap<String, JsonValue<?>>(map);
                }
                dirty = false;
            }
        }

        /**
         * Generates the final JSON object and returns it.
         * 
         * @return The generated JSON object
         */
        public JsonObject create () {
            prepare();
            dirty = true;
            return new JsonObject(Collections.unmodifiableMap(map));
        }
    }
}
