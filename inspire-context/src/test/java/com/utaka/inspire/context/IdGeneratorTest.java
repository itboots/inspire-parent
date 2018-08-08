/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.context;

import com.utaka.inspire.util.IdGenerator;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author XINEN
 */
public class IdGeneratorTest {

    @Test
    public void testGet() {
        Set<String> set = Sets.newHashSet();
        for (int i = 0; i < 1000; i++) {
            String id = IdGenerator.get(5);
            assertThat(set).doesNotContain(id);
            set.add(id);
        }
    }

}
