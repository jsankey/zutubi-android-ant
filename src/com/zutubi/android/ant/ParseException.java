package com.zutubi.android.ant;

/**
 * Exception raised on failure to parse a manifest.
 *
 * @see Manifest
 */
public class ParseException extends Exception {
    private static final long serialVersionUID = 7467884156539661067L;

    /**
     * Creates a new exception with the given error details.
     *
     * @param message context about the error that occurred
     */
    public ParseException(final String message) {
        super(message);
    }

    /**
     * Creates a new exception caused by another exception.
     *
     * @param message context about the error that occurred
     * @param t       the cause of this exception
     */
    public ParseException(final String message, final Throwable t) {
        super(message, t);
    }
}
