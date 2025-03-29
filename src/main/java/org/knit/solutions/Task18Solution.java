package org.knit.solutions;

import org.knit.TaskDescription;
import org.knit.solutions.Task18.DuplicateZeros;

/*
Дан массив целых чисел arr фиксированной длины. Необходимо продублировать каждое вхождение нуля, сдвигая остальные элементы вправо.

Примечание:

Элементы, выходящие за пределы исходного массива, не записываются.
Изменения нужно выполнить на месте (in-place), не возвращая новый массив.
Пример 1:
Вход: arr = [1,0,2,3,0,4,5,0]
Выход: [1,0,0,2,3,0,0,4]
Объяснение:

Ноль после 1 дублируется → [1,0,0,2,3,0,4,5]
Ноль после 3 дублируется → [1,0,0,2,3,0,0,4] (последний 0 выходит за границы и отбрасывается)
Пример 2:
Вход: arr = [1,2,3]
Выход: [1,2,3]
Объяснение: В массиве нет нулей, поэтому он остаётся без изменений.

Ограничения:
1 <= arr.length <= 10^4
0 <= arr[i] <= 9
Напишите решение, а затем Напишите JUnit тесты для проверки решения
Тесты должны покрывать
--Обычные случаи (с нулями и без).

--Краевые случаи (пустые массивы, все нули, нули на границах).

--Производительность (большие массивы) . // просто зафиксировать время выполнения по производительности эмпирическим путем

 * Дублирует каждый ноль в массиве, сдвигая элементы вправо.
 * Элементы, выходящие за границы массива, отбрасываются.
 *
 * @param arr Входной массив (изменяется на месте)

public void duplicateZeros(int[] arr) {

}
 */

@TaskDescription(taskNumber = 18, taskDescription = "Продублировать каждое вхождение нуля, сдвигая остальные элементы вправо")
public class Task18Solution implements Solution {
    @Override
    public void execute() {
        DuplicateZeros dz = new DuplicateZeros();

        int[] arr1 = {1, 0, 2, 3, 0, 4, 5, 0};
        System.out.print("Пример 1: ");
        printArray(arr1);
        dz.duplicateZeros(arr1);
        printArray(arr1);

        int[] arr2 = {1, 2, 3};
        System.out.print("Пример 2: ");
        printArray(arr2);
        dz.duplicateZeros(arr2);
        printArray(arr2);
    }

    private void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i != arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
