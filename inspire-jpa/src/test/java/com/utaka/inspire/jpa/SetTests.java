/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

/**
 * Created by lanxe on 2017/6/2.
 */
public class SetTests {

    @Test
    public void test() {
        Set<String> set = Sets.newHashSet();
        set.addAll(Lists.newArrayList(
                "40287e814ece9b49014ece9b8358000e",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "40287e814ece9b49014ece9b7e1f0009",
                "297ed0d04d9460e9014d946104c30002",
                "402888b450d517860150d517ccc70001",
                "40287e814ece9b49014ece9b7f61000a",
                "40287e814ece9b49014ece9b77870006",
                "40287e814ece9b49014ece9b808d000c",
                "402888b450d517860150d517ccc70001",
                "297ed0d04d93bc0b014d93bc28830005",
                "40287e814ece9b49014ece9b7fd9000b",
                "297ed0d04d93bc0b014d93bc26320000",
                "40287e814efbce91014efbceb4fb0000",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "40287e814efbce91014efbceb4fb0000",
                "40287e814efbce91014efbceb4fb0000",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "297ed0d04d93bc0b014d93bc26ff0001",
                "297ed0d04d93bc0b014d93bc26320000",
                "40287e814ece9b49014ece9b7fd9000b",
                "297ed0d04d9460e9014d946103930000"
        ));
        set.addAll(Lists.newArrayList(
                "297ed0d04d93bc0b014d93bc26ff0001",
                "297ed0d04d93bc0b014d93bc26ff0001",
                "297ed0d04d93bc0b014d93bc26320000",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "297ed0d04d93bc0b014d93bc26320000",
                "297ed0d04d93bc0b014d93bc26ff0001",
                "297ed0d04d93bc0b014d93bc26ff0001",
                "297ed0d04d93bc0b014d93bc26ff0001",
                "40287e814efbce91014efbceb4fb0000",
                "40287e814efbce91014efbceb4fb0000",
                "40287e814efbce91014efbceb4fb0000",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "40287e814ece9b49014ece9b6fbf0002",
                "40287e814ece9b49014ece9b6e7f0001",
                "40287e814ece9b49014ece9b6d540000",
                "297ed0d04d93bc0b014d93bc29610007",
                "297ed0d04d93bc0b014d93bc28830005",
                "40287e814ece9b49014ece9b7fd9000b",
                "297ed0d04d93bc0b014d93bc26320000",
                "40287e814efbce91014efbceb4fb0000",
                "297ed0d04d93bc0b014d93bc2cae000f"
        ));

        set.addAll(Lists.newArrayList(
                "297ed0d04d9460e9014d9461044f0001",
                "40287e814ece9b49014ece9b7dc80008",
                "40287e814efbce91014efbceb4fb0000",
                "40287e814efbce91014efbceb4fb0000",
                "40287e814efbce91014efbceb4fb0000",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "40287e814ece9b49014ece9b6fbf0002",
                "40287e814ece9b49014ece9b6e7f0001",
                "40287e814ece9b49014ece9b6d540000",
                "297ed0d04d93bc0b014d93bc29610007",
                "297ed0d04d93bc0b014d93bc28830005",
                "40287e814ece9b49014ece9b7fd9000b",
                "297ed0d04d93bc0b014d93bc26320000",
                "40287e814efbce91014efbceb4fb0000",
                "297ed0d04d93bc0b014d93bc2cae000f",
                "04954A5260034AA99A26E7F23E208587",
                "66ED692EDC71495F80F41FAE922BADFA",
                "F65C4FB4C01F4167B67BE48F500CD7B8",
                "F65C4FB4C01F4167B67BE48F500CD7B8",
                "66ED692EDC71495F80F41FAE922BADFA",
                "66ED692EDC71495F80F41FAE922BADFA"
        ));

        System.out.println(set.size());

    }
}