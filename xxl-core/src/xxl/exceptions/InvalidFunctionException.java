package xxl.exceptions;

import java.io.Serial;

/**
 * Given function name is invalid.
 */
public class InvalidFunctionException extends Exception {
    /* Class serial number. */
	@Serial
	private static final long serialVersionUID = 202322100248L;

    /* The function name. */
    private final String _function;

	/**
     * @param function name of the function. 
     * */
    public InvalidFunctionException(String function) {
        _function = function;
	}

	/** @return the function name. */
    public String getFunction() {
        return _function;
    }
}