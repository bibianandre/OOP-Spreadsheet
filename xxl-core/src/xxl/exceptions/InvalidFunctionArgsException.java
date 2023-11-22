package xxl.exceptions;

import java.io.Serial;

/**
 * Given function arguments are invalid.
 */
public class InvalidFunctionArgsException extends Exception {
    /* Class serial number. */
	@Serial
	private static final long serialVersionUID = 202327100748L;

    /* The function name. */
    private final String _function;

	/**
     * @param function name of the function. 
     * */
    public InvalidFunctionArgsException(String function) {
        _function = function;
	}

	/** @return the bad function. */
    public String getFunctionArgs() {
        return _function;
    }
}