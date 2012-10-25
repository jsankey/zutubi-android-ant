package com.zutubi.android.ant;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A dotted-decimal version with an arbitrary number of integer elements.
 */
public class Version {
    private static final char SEPARATOR = '.';

    private List<Integer> elements = new LinkedList<Integer>();

    /**
     * Creates a version by parsing a string.
     *
     * @param versionString the string to parse, must be dotted-decimal with no empty elements
     * @throws IllegalArgumentException if the string is not a valid version
     */
    public Version(final String versionString) {
        String element = "";
        for (int i = 0; i < versionString.length(); i++) {
            final char c = versionString.charAt(i);
            if (c == SEPARATOR) {
                addElement(element);
                element = "";
            } else {
                element += c;
            }
        }

        addElement(element);
    }

    private void addElement(final String element) {
        if (element.length() == 0) {
            throw new IllegalArgumentException("Invalid version: empty element at index " + elements.size());
        }

        try {
            elements.add(Integer.parseInt(element));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version: element '" + element + "' at index " + elements.size() + " is not an integer");
        }
    }

    /**
     * @return the elements of the version, from most to least significant
     */
    public List<Integer> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
