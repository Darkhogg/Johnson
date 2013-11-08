package es.darkhogg.johnson.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Representation of a JSON Array.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonArray extends JsonValue<List<JsonValue<?>>> {

    /** An empty JSON array */
    public static final JsonArray EMPTY = new JsonArray(Collections.<JsonValue<?>> emptyList());

    /**
     * Creates a JSON array using the given list.
     * <p>
     * This method does not copy nor wraps the passed list in any way, so any callers <i>MUST</i> ensure that the list
     * remains unmodifiable by their own means.
     * 
     * @param value The list to be wrapped in this JSON array
     */
    private JsonArray (List<JsonValue<?>> value) {
        super(value);
    }

    /**
     * Get the number of elements in this JSON array.
     * 
     * @return The length of this array
     */
    public int getLength () {
        return getValue().size();
    }

    /**
     * Return the array element at the position indicated by <tt>index</tt>.
     * 
     * @param index Array position to return
     * @return The element at the <tt>index</tt> position
     */
    public JsonValue<?> get (int index) {
        return getValue().get(index);
    }

    /**
     * A class used to create JSON arrays element by element.
     * 
     * @author Daniel Escoz
     * @version 1.0
     */
    public static final class Builder {

        /** Internal list used for the builder */
        private ArrayList<JsonValue<?>> list;

        /** Whether the list is already used */
        private boolean dirty;

        /** Creates a new <tt>Builder</tt> with no elements. */
        public Builder () {
            dirty = true;
        }

        /**
         * Creates a new <tt>Builder</tt> with the elements of the given collection.
         * 
         * @param elems Elements to add to this builder
         */
        public Builder (Collection<JsonValue<?>> elems) {
            this();
            addAll(elems);
        }

        /** Prepares this builder. */
        private void prepare () {
            if (dirty) {
                if (list == null) {
                    list = new ArrayList<JsonValue<?>>();
                } else {
                    list = new ArrayList<JsonValue<?>>(list);
                }
                dirty = false;
            }
        }

        /**
         * Adds an element to this JSON array builder.
         * 
         * @param elem The element to be added
         * @return <tt>this</tt>
         */
        public Builder add (JsonValue<?> elem) {
            prepare();
            list.add(elem);
            return this;
        }

        /**
         * Adds multiple elements to this JSON array builder.
         * 
         * @param elems The elements to be added
         * @return <tt>this</tt>
         */
        public Builder addAll (Collection<? extends JsonValue<?>> elems) {
            prepare();
            list.addAll(elems);
            return this;
        }

        /**
         * Generates the final JSON array and returns it.
         * 
         * @return The generated JSON array
         */
        public JsonArray create () {
            prepare();
            dirty = true;
            return new JsonArray(Collections.unmodifiableList(list));
        }
    }
}
