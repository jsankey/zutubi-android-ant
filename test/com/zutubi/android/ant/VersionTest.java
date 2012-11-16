package com.zutubi.android.ant;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

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

    @Test(expected = IllegalArgumentException.class)
    public void testNoElements() {
        new Version(new ArrayList<Integer>());
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

    @Test
    public void testBumpSingleElement() {
        final Version version = new Version("3");
        assertEquals(new Version("4"), version.bump());
    }

    @Test
    public void testBumpMultipleElements() {
        final Version version = new Version("2.6.11");
        assertEquals(new Version("2.6.12"), version.bump());
    }

    @Test
    public void testToStringSingleElement() {
        final Version version = new Version("11");
        assertEquals("11", version.toString());
    }

    @Test
    public void testToStringMultipleElements() {
        final Version version = new Version("11.0.1");
        assertEquals("11.0.1", version.toString());
    }
}
