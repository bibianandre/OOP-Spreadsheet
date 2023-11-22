package xxl.exceptions;

import java.io.Serial;

/**
 * Exception for entries of an unexpected type.
 */
public class UnknownEntryTypeException extends Exception {
	/** Class serial number. */
    @Serial
	private static final long serialVersionUID = 202310091907L;

	/** Unrecognized entry type. */
	private String _entry;

	/**
	 * @param entry
	 */
	public UnknownEntryTypeException(String entry) {
		_entry = entry;
	}

	/**
	 * @param entry
	 * @param cause
	 */
	public UnknownEntryTypeException(String entry, Exception cause) {
		super(cause);
		_entry = entry;
	}

	/**
	 * @return the bad entry.
	 */
	public String getEntry() {
		return _entry;
	}

}
