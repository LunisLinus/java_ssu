package org.knit.solutions;

import org.knit.TaskDescription;
import org.knit.solutions.Task19.MergeSortedArrays;

import java.util.Arrays;

/*
Даны два целочисленных массива nums1 и nums2, отсортированных в неубывающем порядке, а также два целых числа m и n, представляющих количество элементов в nums1 и nums2 соответственно.

Требуется:
Объединить nums1 и nums2 в один массив, отсортированный в неубывающем порядке.

Важные условия:

Итоговый отсортированный массив должен быть сохранён внутри массива nums1 (функция не должна ничего возвращать).
Массив nums1 имеет длину m + n, где:
Первые m элементов — значимые (их нужно объединять с nums2).
Последние n элементов заполнены нулями и должны быть проигнорированы.
Длина nums2 равна n.
Пример 1:
Входные данные:
nums1 = [1, 2, 3, 0, 0, 0], m = 3
nums2 = [2, 5, 6], n = 3

Результат:
[1, 2, 2, 3, 5, 6]

Объяснение:
Объединяемые массивы: [1, 2, 3] и [2, 5, 6].
Результат — [1, 2, 2, 3, 5, 6] (подчёркнутые элементы взяты из nums1).

Пример 2:
Входные данные:
nums1 = [1], m = 1
nums2 = [], n = 0

Результат:
[1]

Объяснение:
Объединяемые массивы: [1] и [].
Результат — [1].

Пример 3:
Входные данные:
nums1 = [0], m = 0
nums2 = [1], n = 1

Результат:
[1]

Объяснение:
Объединяемые массивы: [] и [1].
Поскольку m = 0, в nums1 нет значимых элементов (нули служат только для резерва места).

Ограничения:
nums1.length == m + n
nums2.length == n
0 <= m, n <= 200
1 <= m + n <= 200
-10^9 <= nums1[i], nums2[j] <= 10^9
Примечания:
Нельзя использовать дополнительную память для создания нового массива (кроме временных переменных).
Решение должно работать за линейное время O(m + n).
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {

    }
}
Напишите решение, а затем Напишите JUnit тесты для проверки решения
Тесты должны покрывать
--Обычные случаи (слияние с данными и без).

--Краевые случаи (пустые массивы, все элементы в одном массиве).

--Отрицательные числа и дубликаты.

--Производительность (большие массивы) // просто зафиксировать время выполнения по производительности эмпирическим путем.
 */

@TaskDescription(taskNumber = 19, taskDescription = "Два целочисленных массива nums1 и nums2, отсортированных в неубывающем порядке")
public class Task19Solution implements Solution {
    @Override
    public void execute() {
        System.out.println("Пример 1: ");
        int[] nums1Example1 = {1, 2, 3, 0, 0, 0};
        System.out.println(Arrays.toString(nums1Example1));
        int m1 = 3;
        int[] nums2Example1 = {2, 5, 6};
        System.out.println(Arrays.toString(nums2Example1));
        int n1 = 3;
        MergeSortedArrays.merge(nums1Example1, m1, nums2Example1, n1);
        System.out.println(Arrays.toString(nums1Example1));

        System.out.println("Пример 2: ");
        int[] nums1Example2 = {1};
        System.out.println(Arrays.toString(nums1Example2));
        int m2 = 1;
        int[] nums2Example2 = {};
        System.out.println(Arrays.toString(nums2Example2));
        int n2 = 0;
        MergeSortedArrays.merge(nums1Example2, m2, nums2Example2, n2);
        System.out.println(Arrays.toString(nums1Example2));

        System.out.println("Пример 3: ");
        int[] nums1Example3 = {0};
        System.out.println(Arrays.toString(nums1Example3));
        int m3 = 0;
        int[] nums2Example3 = {1};
        System.out.println(Arrays.toString(nums2Example3));
        int n3 = 1;
        MergeSortedArrays.merge(nums1Example3, m3, nums2Example3, n3);
        System.out.println(Arrays.toString(nums1Example3));

    }
}
