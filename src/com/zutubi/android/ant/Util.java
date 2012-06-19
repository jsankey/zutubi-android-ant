
package com.zutubi.android.ant;

/**
 * Various utility methods.
 */
public class Util {
    /**
     * Tests if a given string is non-null and non-empty.
     *
     * @param s the string to test
     * @return true if the string is non-null and non-empty
     */
    public static boolean stringSet(final String s) {
        return s != null && s.length() > 0;
    }
}
