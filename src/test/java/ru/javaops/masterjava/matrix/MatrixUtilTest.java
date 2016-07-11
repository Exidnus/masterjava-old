package ru.javaops.masterjava.matrix;

import static junit.framework.TestCase.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DVarygin on 11.07.2016.
 */
public class MatrixUtilTest {

    @Test
    public void testIsDegreeOfTwo() throws Exception {
        List<Integer> degreesOfTwo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            degreesOfTwo.add(2 << i);
        }
        for (int i = 0; i < 2000; i++) {
            if (Collections.binarySearch(degreesOfTwo, i) >= 0) {
                assertTrue(MatrixUtil.isDegreeOfTwo(i));
            } else {
                assertFalse(MatrixUtil.isDegreeOfTwo(i));
            }
        }
    }

}