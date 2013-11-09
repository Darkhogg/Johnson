package es.darkhogg.johnson.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * A class used to read JSON from character streams. Note that this class does not provide high-level reading of
 * {@link es.darkhogg.johnson.data.JsonValue}s, but instead allows JSON to be read at the token level.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonReader implements Closeable {

    /** Underlying reader */
    private final Reader reader;

    /**
     * Creates a JSON reader using the passed <tt>reader</tt> object.
     * 
     * @param reader Reader used for this JSON reader
     */
    public JsonReader (Reader reader) {
        if (reader == null) {
            throw new NullPointerException("reader");
        }

        this.reader = reader;
    }
    
    

    /**
     * Closes the underlying reader.
     * 
     * @throws IOException If the underlying reader throws it
     * @see java.io.Reader#close
     */
    @Override
    public void close () throws IOException {
        reader.close();
    }
}
