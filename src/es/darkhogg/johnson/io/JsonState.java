package es.darkhogg.johnson.io;

import java.util.Arrays;

final class JsonState {

    /** No more values may be written on the stream */
    final static byte EMPTY = 0;

    /** First value to be written */
    final static byte TOP_VALUE = 1;

    /** Expecting the first array element */
    final static byte ARRAY_VALUE_FIRST = 2;

    /** Expecting an array element */
    final static byte ARRAY_VALUE = 3;

    /** Expecting the first object key */
    final static byte OBJECT_KEY_FIRST = 4;

    /** Expecting an object key */
    final static byte OBJECT_KEY = 5;

    /** Expecting an object value */
    final static byte OBJECT_VALUE = 6;

    /** Internal state array used as a stack */
    private byte[] stateStack;

    /** Top of the stack (full-ascending) */
    private int stateTop;

    /** Whether the next value must be written after a comma */
    private boolean commaNeeded;

    public void begin () {
        stateStack = null;
        pushState(TOP_VALUE);
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
     * @return The current top of the state stack, or {@link #EMPTY} if there are no states in the stack.
     */
    public byte getState () {
        if (stateStack == null || stateTop < 0) {
            return EMPTY;

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

    /** @return Whether a comma is needed before the next token */
    public boolean isCommaNeeded () {
        return commaNeeded;
    }

    /**
     * Performs the state transitions for the case that a value is written.
     * 
     * @throws IllegalStateException If this writer did not expect a value
     */
    public void value () {
        switch (getState()) {
            case TOP_VALUE: {
                // Top value -- write it, then stop
                commaNeeded = false;
                popState();
                break;
            }

            case ARRAY_VALUE_FIRST: {
                // Array value -- continue being an array
                commaNeeded = false;
                popState();
                pushState(ARRAY_VALUE);
                break;
            }

            case ARRAY_VALUE: {
                // Array value -- continue being an array
                // we need a comma!
                commaNeeded = true;
                break;
            }

            case OBJECT_KEY_FIRST:
            case OBJECT_KEY: {
                // Object key -- a value is NOT allowed here
                throw new IllegalStateException("JSON: Expecting an object key, given a value");
            }

            case OBJECT_VALUE: {
                // Object value -- next, we need a key
                commaNeeded = false;
                popState();
                pushState(OBJECT_KEY);
                break;
            }

            case EMPTY:
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
    public void key () {
        switch (getState()) {
            case TOP_VALUE:
            case ARRAY_VALUE_FIRST:
            case ARRAY_VALUE:
            case OBJECT_VALUE: {
                // Any kind of value -- not allowed
                throw new IllegalStateException("JSON: Expecting a value, given an object key");
            }

            case OBJECT_KEY_FIRST: {
                // Object key -- continue with a value
                commaNeeded = false;
                popState();
                pushState(OBJECT_VALUE);
                break;
            }

            case OBJECT_KEY: {
                // Object key -- continue with a value
                // we need a comma!
                commaNeeded = true;
                popState();
                pushState(OBJECT_VALUE);
                break;
            }

            case EMPTY:
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
    public void beginArray () {
        switch (getState()) {
            case TOP_VALUE:
            case ARRAY_VALUE_FIRST:
            case OBJECT_VALUE: {
                // Value -- push the array
                commaNeeded = false;
                pushState(ARRAY_VALUE_FIRST);
                break;
            }

            case ARRAY_VALUE: {
                // Value -- push the array
                // we need a comma!
                commaNeeded = true;
                pushState(ARRAY_VALUE_FIRST);
                break;
            }

            case OBJECT_KEY_FIRST:
            case OBJECT_KEY: {
                // Object key -- wrong
                throw new IllegalStateException("JSON: Expecting and object key, given an array beginning");
            }

            case EMPTY:
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
    public void endArray () {
        switch (getState()) {
            case ARRAY_VALUE_FIRST:
            case ARRAY_VALUE: {
                // Array value -- can close the array!
                commaNeeded = false;
                popState();
                value(); // Safe -- we checked there was a value state before
                break;
            }

            case TOP_VALUE:
            case OBJECT_VALUE: {
                throw new IllegalStateException("JSON: Expecting a value, given an array ending");
            }

            case OBJECT_KEY_FIRST:
            case OBJECT_KEY: {
                throw new IllegalStateException("JSON: Expecting an object key, given an array ending");
            }

            case EMPTY:
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
    public void beginObject () {
        switch (getState()) {
            case TOP_VALUE:
            case ARRAY_VALUE_FIRST:
            case OBJECT_VALUE: {
                // Value -- push the array
                pushState(OBJECT_KEY_FIRST);
                break;
            }

            case ARRAY_VALUE: {
                // Value -- push the array
                // we need a comma!
                commaNeeded = true;
                pushState(OBJECT_KEY_FIRST);
                break;
            }

            case OBJECT_KEY_FIRST:
            case OBJECT_KEY: {
                // Object key -- wrong
                throw new IllegalStateException("JSON: Expecting and object key, given an array beginning");
            }

            case EMPTY:
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
    public void endObject () {
        switch (getState()) {
            case ARRAY_VALUE_FIRST:
            case ARRAY_VALUE: {
                throw new IllegalStateException("JSON: Expecting an array value, given an object ending");
            }

            case TOP_VALUE:
            case OBJECT_VALUE: {
                throw new IllegalStateException("JSON: Expecting a value, given an object ending");
            }

            case OBJECT_KEY_FIRST:
            case OBJECT_KEY: {
                // Object key -- can close the object!
                popState();
                value(); // Safe -- we checked there was a value state before
                break;
            }

            case EMPTY:
            default: {
                throw new IllegalStateException("JSON: Expecting EOF, given an object ending");
            }
        }
    }

}
