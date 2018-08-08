/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author lanxe
 */
public class PathTests {

    @Test
    public void testConcat() {

        Matcher<String> matcher = equalTo(new File("/foo/bar").getPath());
        assertThat(
                Paths.concat("/foo/", "bar").newFile().getPath(),
                matcher
        );

        assertThat(
                Paths.concat("/foo", "bar").newFile().getPath(),
                matcher
        );

        assertThat(
                Paths.concat("/foo", "/bar").newFile().getPath(),
                matcher
        );

        matcher = equalTo(new File("c:/foo/bar").getPath());
        assertThat(
                Paths.concat("c:/foo/", "bar").newFile().getPath(),
                matcher
        );

        assertThat(
                Paths.concat("c:/foo", "bar").newFile().getPath(),
                matcher
        );

        assertThat(
                Paths.concat("c:/foo", "/bar").newFile().getPath(),
                matcher
        );

        matcher = equalTo(new File("/foo/bar/c.txt").getPath());

        assertThat(
                Paths.concat("/foo", "bar/c.txt").newFile().getPath(),
                matcher
        );

        assertThat(
                Paths.concat("/foo", "/bar/c.txt").newFile().getPath(),
                matcher
        );

    }
}
