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

    /**
     * Creates a new version with the given elements.
     *
     * @param elements elements of the version, from most to least significant, must not be empty
     * @throws IllegalArgumentException if the given list of elements is empty
     */
    public Version(final List<Integer> elements) {
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("Versions must have at least one element");
        }

        this.elements = elements;
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

    /**
     * Bumps the last element of this version, returning the new version.
     *
     * @return a version just larger than this one
     */
    public Version bump() {
        final List<Integer> bumpedElements = new LinkedList<Integer>(elements);
        final int lastIndex = bumpedElements.size() - 1;
        bumpedElements.set(lastIndex, bumpedElements.get(lastIndex) + 1);
        return new Version(bumpedElements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        return elements.equals(other.elements);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(16);
        for (final Integer element : elements) {
            if (builder.length() > 0) {
                builder.append(SEPARATOR);
            }
            builder.append(element);
        }

        return builder.toString();
    }
}
