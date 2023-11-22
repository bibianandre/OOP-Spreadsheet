package xxl.exceptions;

import java.io.Serial;

/**
 * Dimension to create a new spreadsheet is invalid.
 */
public class InvalidDimensionException extends Exception {
    /* Class serial number. */
	@Serial
	private static final long serialVersionUID = 202310071905L;

    /* The spreadsheet dimension. */
    private final int _dimension;

	/* @param dimension of the spreadsheet (line or column). */
    public InvalidDimensionException(int dimension) {
            _dimension = dimension;
	}

	/** @return the dimension */
    public int getDimension() {
        return _dimension;
    }
}

