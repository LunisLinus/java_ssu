package org.knit;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Assert;
import org.junit.Test;
import org.knit.solutions.Task19.MergeSortedArrays;

public class MergeSortedArraysTest {

    @Test
    public void testExample1() {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        int m = 3;
        int[] nums2 = {2, 5, 6};
        int n = 3;
        MergeSortedArrays.merge(nums1, m, nums2, n);
        assertArrayEquals(new int[]{1, 2, 2, 3, 5, 6}, nums1);
    }

    @Test
    public void testExample2() {
        int[] nums1 = {1};
        int m = 1;
        int[] nums2 = {};
        int n = 0;
        MergeSortedArrays.merge(nums1, m, nums2, n);
        assertArrayEquals(new int[]{1}, nums1);
    }


    @Test
    public void testExample3() {
        int[] nums1 = {0};
        int m = 0;
        int[] nums2 = {1};
        int n = 1;
        MergeSortedArrays.merge(nums1, m, nums2, n);
        assertArrayEquals(new int[]{1}, nums1);
    }

    @Test
    public void testNegativeNumbersAndDuplicates() {
        int[] nums1 = {-3, -1, 0, 0, 0, 0};
        int m = 2;
        int[] nums2 = {-2, 0, 0, 0};
        int n = 4;
        MergeSortedArrays.merge(nums1, m, nums2, n);
        assertArrayEquals(new int[]{-3, -2, -1, 0, 0, 0}, nums1);
    }

    @Test
    public void testPerformance() {
        int m = 100000;
        int n = 100000;
        int[] nums1 = new int[m + n];
        int[] nums2 = new int[n];

        for (int i = 0; i < m; i++) {
            nums1[i] = i;
        }
        for (int j = 0; j < n; j++) {
            nums2[j] = j;
        }

        long start = System.currentTimeMillis();
        MergeSortedArrays.merge(nums1, m, nums2, n);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Время выполнения теста производительности: " + duration + " ms");

        Assert.assertTrue(
                "Тест не пройден: время выполнения " + duration + " ms > 5 ms",
                duration <= 5
        );
    }
}
