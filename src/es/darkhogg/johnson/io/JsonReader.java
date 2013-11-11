package es.darkhogg.johnson.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * A class used to read JSON from character streams. Note that this class does
 * not provide high-level reading of {@link es.darkhogg.johnson.data.JsonValue}
 * s, but instead allows JSON to be read at the token level.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class JsonReader implements Closeable {

	/** Token identifier for JSON null values */
	public static final int TOKEN_NULL = 1;

	/** Token identifier for JSON booloan values */
	public static final int TOKEN_BOOLEAN = 2;

	/** Token identifier for JSON number values */
	public static final int TOKEN_NUMBER = 3;

	/** Token identifier for JSON string values */
	public static final int TOKEN_STRING = 4;

	/** Token identifier for JSON array beginnings */
	public static final int TOKEN_ARRAY_BEGIN = 5;

	/** Token identifier for JSON array endings */
	public static final int TOKEN_ARRAY_END = 6;

	/** Token identifier for JSON object beginnings */
	public static final int TOKEN_OBJECT_BEGIN = 7;

	/** Token identifier for JSON object endings */
	public static final int TOKEN_OBJECT_END = 8;

	/** Token identifier for the end of the document */
	public static final int TOKEN_EOF = 0;

	/** Underlying reader */
	private final Reader reader;

	/** State object */
	private final JsonState state = new JsonState();

	/**
	 * Creates a JSON reader using the passed <tt>reader</tt> object.
	 * 
	 * @param reader
	 *            Reader used for this JSON reader
	 */
	public JsonReader(Reader reader) {
		if (reader == null) {
			throw new NullPointerException("reader");
		}

		this.reader = reader;

		state.begin();
	}

	public int next() {
		return -1;
	}

	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException
	 *             If the underlying reader throws it
	 * @see java.io.Reader#close
	 */
	@Override
	public void close() throws IOException {
		reader.close();
	}
}
