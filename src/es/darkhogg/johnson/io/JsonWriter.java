package es.darkhogg.johnson.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * A class used to write JSON to character streams. Note that this class does not provide high-level writing of
 * {@link es.darkhogg.johnson.data.JsonValue}s, but instead allows JSON to be written at the token level.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonWriter implements Closeable {

    /** No more values may be written on the stream */
    private final static byte STATE_EMPTY = 0;

    /** First value tobe written */
    private final static byte STATE_TOP_VALUE = 1;

    /** Expecting the first array element */
    private final static byte STATE_ARRAY_VALUE_FIRST = 2;

    /** Expecting an array element */
    private final static byte STATE_ARRAY_VALUE = 3;

    /** Expecting the first object key */
    private final static byte STATE_OBJECT_KEY_FIRST = 4;

    /** Expecting an object key */
    private final static byte STATE_OBJECT_KEY = 5;

    /** Expecting an object value */
    private final static byte STATE_OBJECT_VALUE = 6;

    /** Underlying writer */
    private final Writer writer;

    /** Internal state array used as a stack */
    private byte[] stateStack;

    /** Top of the stack (full-ascending) */
    private int stateTop;

    /** Whether the next value must be written after a coma */
    private boolean commaNeeded;

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

        pushState(STATE_TOP_VALUE);
    }

    /**
     * Pushes a state into the internal state stack, resizing it if necessary.
     * 
     * @param state State to push
     */
    private void pushState (byte state) {
        int newTop = stateTop + 1;

        if (stateStack == null) {
            stateStack = new byte[7];
            newTop = 0;

        } else if (newTop >= stateStack.length) {
            stateStack = Arrays.copyOf(stateStack, stateStack.length * 2 + 1);
        }

        stateStack[newTop] = state;
        stateTop = newTop;
    }

    /**
     * @return The current top of the state stack, or {@link JsonWriter#STATE_EMPTY} if there are no states in the
     *         stack.
     */
    private byte getState () {
        if (stateStack == null || stateTop < 0) {
            return STATE_EMPTY;

        } else {
            return stateStack[stateTop];
        }
    }

    /**
     * Pops a value from the stack.
     * 
     * @return The previous stack top
     * @throws IllegalStateException if the stack is empty
     */
    private byte popState () {
        if (stateTop < 0) {
            throw new IllegalStateException("Empty stack");
        }
        return stateStack[stateTop--];
    }

    /**
     * Performs the state transitions for the case that a value is written.
     * 
     * @throws IllegalStateException If this writer did not expect a value
     */
    private void syncValue () {
        switch (getState()) {
            case STATE_TOP_VALUE: {
                // Top value -- write it, then stop
                popState();
                break;
            }

            case STATE_ARRAY_VALUE_FIRST: {
                // Array value -- continue being an array
                popState();
                pushState(STATE_ARRAY_VALUE);
                break;
            }

            case STATE_ARRAY_VALUE: {
                // Array value -- continue being an array
                // we need a comma!
                commaNeeded = true;
                break;
            }

            case STATE_OBJECT_KEY_FIRST:
            case STATE_OBJECT_KEY: {
                // Object key -- a value is NOT allowed here
                throw new IllegalStateException("JSON: Expecting an object key, given a value");
            }

            case STATE_OBJECT_VALUE: {
                // Object value -- next, we need a key
                popState();
                pushState(STATE_OBJECT_KEY);
                break;
            }

            case STATE_EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given a value");
            }
        }
    }

    /**
     * Performs the state transitions for the case that an object key is written.
     * 
     * @throws IllegalStateException If this writer did not expect a key
     */
    private void syncKey () {
        switch (getState()) {
            case STATE_TOP_VALUE:
            case STATE_ARRAY_VALUE_FIRST:
            case STATE_ARRAY_VALUE:
            case STATE_OBJECT_VALUE: {
                // Any kind of value -- not allowed
                throw new IllegalStateException("JSON: Expecting a value, given an object key");
            }

            case STATE_OBJECT_KEY_FIRST: {
                // Object key -- continue with a value
                popState();
                pushState(STATE_OBJECT_VALUE);
                break;
            }

            case STATE_OBJECT_KEY: {
                // Object key -- continue with a value
                // we need a comma!
                commaNeeded = true;
                popState();
                pushState(STATE_OBJECT_VALUE);
                break;
            }

            case STATE_EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given an object key");
            }
        }
    }

    /**
     * Performs the state transitions for the case that an array beginning is written.
     * 
     * @throws IllegalStateException If this writer did not expect an array beginning
     */
    private void syncBeginArray () {
        switch (getState()) {
            case STATE_TOP_VALUE:
            case STATE_ARRAY_VALUE_FIRST:
            case STATE_OBJECT_VALUE: {
                // Value -- push the array
                pushState(STATE_ARRAY_VALUE_FIRST);
                break;
            }

            case STATE_ARRAY_VALUE: {
                // Value -- push the array
                // we need a comma!
                commaNeeded = true;
                pushState(STATE_ARRAY_VALUE_FIRST);
                break;
            }

            case STATE_OBJECT_KEY_FIRST:
            case STATE_OBJECT_KEY: {
                // Object key -- wrong
                throw new IllegalStateException("JSON: Expecting and object key, given an array beginning");
            }

            case STATE_EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given an array beginning");
            }
        }
    }

    /**
     * Performs the state transitions for the case that an array ending is written.
     * 
     * @throws IllegalStateException If this writer did not expect an array ending
     */
    private void syncEndArray () {
        switch (getState()) {
            case STATE_ARRAY_VALUE_FIRST:
            case STATE_ARRAY_VALUE: {
                // Array value -- can close the array!
                popState();
                syncValue(); // Safe -- we checked there was a value state before
                break;
            }

            case STATE_TOP_VALUE:
            case STATE_OBJECT_VALUE: {
                throw new IllegalStateException("JSON: Expecting a value, given an array ending");
            }

            case STATE_OBJECT_KEY_FIRST:
            case STATE_OBJECT_KEY: {
                throw new IllegalStateException("JSON: Expecting an object key, given an array ending");
            }

            case STATE_EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given an array ending");
            }
        }
    }

    /**
     * Performs the state transitions for the case that an object beginning is written.
     * 
     * @throws IllegalStateException If this writer did not expect an object beginning
     */
    private void syncBeginObject () {
        switch (getState()) {
            case STATE_TOP_VALUE:
            case STATE_ARRAY_VALUE_FIRST:
            case STATE_OBJECT_VALUE: {
                // Value -- push the array
                pushState(STATE_OBJECT_KEY_FIRST);
                break;
            }

            case STATE_ARRAY_VALUE: {
                // Value -- push the array
                // we need a comma!
                commaNeeded = true;
                pushState(STATE_OBJECT_KEY_FIRST);
                break;
            }

            case STATE_OBJECT_KEY_FIRST:
            case STATE_OBJECT_KEY: {
                // Object key -- wrong
                throw new IllegalStateException("JSON: Expecting and object key, given an array beginning");
            }

            case STATE_EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given an array beginning");
            }
        }
    }

    /**
     * Performs the state transitions for the case that an object ending is written.
     * 
     * @throws IllegalStateException If this writer did not expect an object ending
     */
    private void syncEndObject () {
        switch (getState()) {
            case STATE_ARRAY_VALUE_FIRST:
            case STATE_ARRAY_VALUE: {
                throw new IllegalStateException("JSON: Expecting an array value, given an object ending");
            }

            case STATE_TOP_VALUE:
            case STATE_OBJECT_VALUE: {
                throw new IllegalStateException("JSON: Expecting a value, given an object ending");
            }

            case STATE_OBJECT_KEY_FIRST:
            case STATE_OBJECT_KEY: {
                // Object key -- can close the object!
                popState();
                syncValue(); // Safe -- we checked there was a value state before
                break;
            }

            case STATE_EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given an object ending");
            }
        }
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
     * Writes a comma to the writer only if indicated internally by the {@link #commaNeeded} value.
     * <p>
     * Resets that value to <tt>false</tt>.
     * 
     * @throws IOException If an I/O error happens
     */
    private void writeCommaIfNeeded () throws IOException {
        if (commaNeeded) {
            writeToWriter(',');
        }

        commaNeeded = false;
    }

    /**
     * Writes a JSON <tt>null</tt> value to the stream.
     * 
     * @return <tt>this</tt>
     * @throws IOException If some I/O error happens
     * @throws IllegalStateException If this writer did not expect a value
     */
    public JsonWriter valueNull () throws IOException {
        syncValue();
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
        syncValue();
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
        syncValue();
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
        syncValue();
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
        syncValue();
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
        syncValue();
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
        syncValue();
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
        syncValue();
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
        syncKey();
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
        syncBeginArray();
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
        syncEndArray();
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
        syncBeginObject();
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
        syncEndObject();
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
        byte state;
        while ((state = getState()) != STATE_EMPTY) {
            switch (state) {
                case STATE_TOP_VALUE:
                case STATE_OBJECT_VALUE: {
                    valueNull();
                    break;
                }

                case STATE_ARRAY_VALUE:
                case STATE_ARRAY_VALUE_FIRST: {
                    endArray();
                    break;
                }

                case STATE_OBJECT_KEY:
                case STATE_OBJECT_KEY_FIRST: {
                    endObject();
                    break;
                }
            }
        }

        stateStack = null;
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
