package com.zutubi.android.ant;

import org.apache.tools.ant.BuildException;

import java.util.LinkedList;
import java.util.List;

/**
 * A task to set arbitrary manifest attributes to new values.
 */
public class SetManifestAttributesTask extends AbstractManifestUpdateTask {
    private final List<Attribute> attributes = new LinkedList<Attribute>();

    @Override
    protected void updateManifest(final Manifest manifest) {
        for (final Attribute attribute : attributes) {
            manifest.setAttributeValue(attribute.getName(), attribute.getValue());
        }
    }

    /**
     * Ant callback to add a nested attribute.
     *
     * @param attribute the attribute to add
     */
    public void addConfiguredAttribute(final Attribute attribute) {
        if (!Util.stringSet(attribute.getName())) {
            throw new BuildException("Attribute name must be specified and not empty.");
        }

        if (attribute.getValue() == null) {
            throw new BuildException("Attribute value must be specified.");
        }

        attributes.add(attribute);
    }

    /**
     * Nested element to specify a new value for a single attribute.
     */
    public static class Attribute {
        private String name;
        private String value;

        /**
         * Default constructor for Ant.
         */
        public Attribute() {
        }

        /**
         * Creates an attribute with the given details.
         *
         * @param name name of the attribute to update
         * @param value new attribute value
         */
        public Attribute(final String name, final String value) {
            super();
            this.name = name;
            this.value = value;
        }

        /**
         * @return the name of the attribute to update
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the attribute to update.
         *
         * @param name attribute name
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * @return the new attribute value to set
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the new value to apply to the attribute.
         *
         * @param value the new attribute value
         */
        public void setValue(final String value) {
            this.value = value;
        }
    }
}
