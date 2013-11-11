package es.darkhogg.johnson.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

/**
 * A class used to write JSON to character streams. Note that this class does not provide high-level writing of
 * {@link es.darkhogg.johnson.data.JsonValue}s, but instead allows JSON to be written at the token level.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonWriter implements Closeable {

    /** Underlying writer */
    private final Writer writer;

    /** State structure */
    private final JsonState state = new JsonState();

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

        state.begin();
    }

    /**
     * Writes the specified string to the underlying writer.
     * 
     * @param string String to be written
     * @throws IOException If an I/O error happens
     * @see Writer#write(String)
     */
    private void writeToWriter (String string) throws IOException {
        writer.write(string);
    }

    /**
     * Writes the specified character to the underlying writer.
     * 
     * @param chr Character to be written
     * @throws IOException If an I/O error happens
     * @see Writer#write(int)
     */
    private void writeToWriter (int chr) throws IOException {
        writer.write(chr);
    }

    /**
     * Writes a comma to the writer only if indicated internally by the {@link JsonState#isCommaNeeded} value.
     * <p>
     * Resets that value to <tt>false</tt>.
     * 
     * @throws IOException If an I/O error happens
     */
    private void writeCommaIfNeeded () throws IOException {
        if (state.isCommaNeeded()) {
            writeToWriter(',');
        }
    }

    /**
     * Writes a JSON <tt>null</tt> value to the stream.
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter valueNull () throws IOException {
        state.value();
        writeCommaIfNeeded();
        writeToWriter("null");
        return this;
    }

    /**
     * Writes a JSON <i>boolean</i> value to the stream.
     * 
     * @param bool Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (boolean bool) throws IOException {
        state.value();
        writeCommaIfNeeded();
        writeToWriter(bool ? "true" : "false");
        return this;
    }

    /**
     * Writes a JSON <i>number</i> value to the stream.
     * 
     * @param number Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (int number) throws IOException {
        state.value();
        writeCommaIfNeeded();
        writeToWriter(String.valueOf(number));
        return this;
    }

    /**
     * Writes a JSON <i>number</i> value to the stream.
     * 
     * @param number Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (long number) throws IOException {
        state.value();
        writeCommaIfNeeded();
        writeToWriter(String.valueOf(number));
        return this;
    }

    /**
     * Writes a JSON <i>number</i> value to the stream.
     * 
     * @param number Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (float number) throws IOException {
        state.value();
        writeCommaIfNeeded();
        writeToWriter(String.valueOf(number));
        return this;
    }

    /**
     * Writes a JSON <i>number</i> value to the stream.
     * 
     * @param number Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (double number) throws IOException {
        state.value();
        writeCommaIfNeeded();
        writeToWriter(String.valueOf(number));
        return this;
    }

    /**
     * Writes a JSON <i>number</i> value to the stream.
     * 
     * @param number Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (Number number) throws IOException {
        if (number == null) {
            throw new NullPointerException("number");
        }
        state.value();
        writeCommaIfNeeded();
        writeToWriter(number.toString());
        return this;
    }

    /**
     * Writes a JSON <i>string</i> value to the stream.
     * 
     * @param string Value to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter value (String string) throws IOException {
        if (string == null) {
            throw new NullPointerException("string");
        }
        state.value();
        writeCommaIfNeeded();
        writeToWriter(encodeString(string));
        return this;
    }

    /**
     * Writes a JSON <i>string</i> key to the stream.
     * 
     * @param string Key to be written
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a key
     */
    public JsonWriter key (String string) throws IOException {
        if (string == null) {
            throw new NullPointerException("string");
        }
        state.key();
        writeCommaIfNeeded();
        writeToWriter(encodeString(string));
        writeToWriter(':');
        return this;
    }

    /**
     * Begins writing a JSON <i>array</i> to the stream.
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter beginArray () throws IOException {
        state.beginArray();
        writeCommaIfNeeded();
        writeToWriter('[');
        return this;
    }

    /**
     * Ends writing a JSON <i>array</i> to the stream.
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect an array ending
     */
    public JsonWriter endArray () throws IOException {
        state.endArray();
        writeToWriter(']');
        return this;
    }

    /**
     * Begins writing a JSON <i>object</i> to the stream.
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter beginObject () throws IOException {
        state.beginObject();
        writeCommaIfNeeded();
        writeToWriter('{');
        return this;
    }

    /**
     * Ends writing a JSON <i>object</i> to the stream.
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect an object ending
     */
    public JsonWriter endObject () throws IOException {
        state.endObject();
        writeToWriter('}');
        return this;
    }

    /**
     * Ends any open array or object and writes a JSON <tt>null</tt> if a value is required to complete a JSON value.
     * <p>
     * Note that this method is guaranteed not to throw an <tt>IllegalStateException</tt> and will make the written
     * object a complete and correct JSON value.
     * <p>
     * It's recommended to call this method before calling {@link #close} if the exact status of the stream is not
     * known, in order to complete a valid JSON value.
     * 
     * @return <tt>this</tt>
     * @throws IOException If an I/O error happens
     */
    public JsonWriter endAll () throws IOException {
        byte stateb;
        while ((stateb = state.getState()) != JsonState.EMPTY) {
            switch (stateb) {
                case JsonState.TOP_VALUE:
                case JsonState.OBJECT_VALUE: {
                    valueNull();
                    break;
                }

                case JsonState.ARRAY_VALUE:
                case JsonState.ARRAY_VALUE_FIRST: {
                    endArray();
                    break;
                }

                case JsonState.OBJECT_KEY:
                case JsonState.OBJECT_KEY_FIRST: {
                    endObject();
                    break;
                }
            }
        }

        return this;
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

    /**
     * Encodes a string as a JSON string.
     * 
     * @param toEncode String to be encoded
     * @return JSON representation of the stream
     */
    private static final String encodeString (String toEncode) {
        StringBuilder sb = new StringBuilder(toEncode.length() * 5 / 2).append('"');
        for (int i = 0; i < toEncode.length(); i++) {
            char c = toEncode.charAt(i);

            if (c < 32 || c > 255) {
                sb.append(String.format("\\u%04x", c));
            }
        }
        return sb.append('"').toString();
    }

}
