package es.darkhogg.johnson.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * A class used to write JSON to character streams. Note that this class does not provide high-level writing of
 * {@link es.darkhogg.johnson.JsonValue}s, but instead allows JSON to be written at the token level.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonWriter implements Closeable {

    private final Writer writer;

    /**
     * Creates a JSON writer using the passed <tt>writer</tt> object.
     * 
     * @param writer Writer used for this JSON writer
     */
    public JsonWriter (Writer writer) {
        if (writer == null) {
            throw new NullPointerException("writer");
        }
        this.writer = writer;
    }

    public void nullValue () throws IOException {
        writer.write("null");
    }

    public void value (boolean bool) throws IOException {
        writer.write(bool ? "true" : "false");
    }

    public void value (int number) throws IOException {
        writer.write(String.valueOf(number));
    }

    public void value (long number) throws IOException {
        writer.write(String.valueOf(number));
    }

    public void value (float number) throws IOException {
        writer.write(String.valueOf(number));
    }

    public void value (double number) throws IOException {
        writer.write(String.valueOf(number));
    }

    public void value (Number number) throws IOException {
        if (number == null) {
            throw new NullPointerException("number");
        }
        writer.write(number.toString());
    }

    public void value (String string) throws IOException {
        if (string == null) {
            throw new NullPointerException("string");
        }
        writer.write(encodeString(string));
    }

    public void key (String string) throws IOException {
        if (string == null) {
            throw new NullPointerException("string");
        }
        
        writer.write(encodeString(string));
    }

    public void beginArray () throws IOException {
        writer.write('[');
    }

    public void endArray () throws IOException {
        writer.write(']');
    }

    public void beginObject () throws IOException {
        writer.write('{');
    }

    public void endObject () throws IOException {
        writer.write('}');
    }

    /**
     * Closes the underlying writer object.
     * 
     * @throws IOException If the underlying writer throws it
     * @see java.io.Writer#close
     */
    public void close () throws IOException {
        writer.close();
    }
    
    
    private static final String encodeString (String toEncode) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < toEncode.length(); i++) {
            char c = toEncode.charAt(i);
            
            if (c < 32 || c > 255) {
                // TODO Encode as \\uXXXX
            }
        }
        return sb.append("\"").toString();
    }
}
