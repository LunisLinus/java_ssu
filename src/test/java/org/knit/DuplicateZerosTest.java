package org.knit;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import org.knit.solutions.Task18.DuplicateZeros;

public class DuplicateZerosTest {

    @Test
    public void testExample1() {
        DuplicateZeros dz = new DuplicateZeros();
        int[] arr = {1, 0, 2, 3, 0, 4, 5, 0};
        dz.duplicateZeros(arr);
        assertArrayEquals(new int[]{1, 0, 0, 2, 3, 0, 0, 4}, arr);
    }

    @Test
    public void testExample2() {
        DuplicateZeros dz = new DuplicateZeros();
        int[] arr = {1, 2, 3};
        dz.duplicateZeros(arr);
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }

    @Test
    public void testAllZeros() {
        DuplicateZeros dz = new DuplicateZeros();
        int[] arr = {0, 0, 0, 0};
        dz.duplicateZeros(arr);
        assertArrayEquals(new int[]{0, 0, 0, 0}, arr);
    }

    @Test
    public void testZerosAtBoundaries() {
        DuplicateZeros dz = new DuplicateZeros();
        int[] arr = {0, 1, 2, 3, 4, 0};
        dz.duplicateZeros(arr);
        assertArrayEquals(new int[]{0, 0, 1, 2, 3, 4}, arr);
    }

    @Test
    public void testEmptyArray() {
        DuplicateZeros dz = new DuplicateZeros();
        int[] arr = new int[0];
        dz.duplicateZeros(arr);
        assertArrayEquals(new int[0], arr);
    }

    @Test
    public void testLargeArrayPerformance() {
        DuplicateZeros dz = new DuplicateZeros();
        int size = 1000000;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (i % 10 == 0) ? 0 : i % 10;
        }

        long start = System.currentTimeMillis();
        dz.duplicateZeros(arr);
        long duration = System.currentTimeMillis() - start;

        System.out.println("Время выполнения большого массива: " + duration + " ms");
    }
}
