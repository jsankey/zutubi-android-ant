package com.zutubi.android.ant;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class VersionTest {
    @Test(expected = IllegalArgumentException.class)
    public void testParseEmpty() {
        new Version("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDot() {
        new Version(".");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidElement() {
        new Version("1.hello.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyElement() {
        new Version("1..2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyElementFirst() {
        new Version(".1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyElementLast() {
        new Version("1.2.");
    }

    @Test
    public void testParseSingleElement() {
        final Version version = new Version("42");
        assertEquals(asList(42), version.getElements());
    }

    @Test
    public void testParseMultipleElements() {
        final Version version = new Version("2.4.54");
        assertEquals(asList(2, 4, 54), version.getElements());
    }

    @Test
    public void testParseNegativeElement() {
        final Version version = new Version("22.0.-1");
        assertEquals(asList(22, 0, -1), version.getElements());
    }
}
