package xxl.exceptions;

import java.io.Serial;

/**
 * Dimension to create a new spreadsheet is invalid.
 */
public class InvalidCellRefException extends Exception {
    /* Class serial number. */
	@Serial
	private static final long serialVersionUID = 202310091723L;

    /* The spreadsheet dimension. */
    private final String _ref;

	/* @param dimension of the spreadsheet (line or column). */
    public InvalidCellRefException(String ref) {
            _ref = ref;
	}

	/** @return the dimension */
    public String getRef() {
        return _ref;
    }
}