package com.quchen.flashcard;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void listFileItemGetLabel_Test() throws Exception {
        ListFileItem listFileItem1 = new ListFileItem("foo", "foo.csv");
        assertEquals(listFileItem1.getlabel(), "foo");

        ListFileItem listFileItem2 = new ListFileItem("foo", "foo");
        assertEquals(listFileItem2.getlabel(), "foo");
    }
}