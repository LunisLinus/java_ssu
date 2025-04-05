package org.knit.solutions.Task18;

public class DuplicateZeros {
    public void duplicateZeros(int[] arr) {
        int possibleDups = 0;
        int length = arr.length - 1;

        for (int left = 0; left <= length - possibleDups; left++) {
            if (arr[left] == 0) {
                if (left == length - possibleDups) {
                    arr[length] = 0;
                    length--;
                    break;
                }
                possibleDups++;
            }
        }

        int last = length - possibleDups;

        for (int i = last; i >= 0; i--) {
            if (arr[i] == 0) {
                if (i + possibleDups < arr.length) {
                    arr[i + possibleDups] = 0;
                }
                possibleDups--;
                if (i + possibleDups < arr.length) {
                    arr[i + possibleDups] = 0;
                }
            } else {
                if (i + possibleDups < arr.length) {
                    arr[i + possibleDups] = arr[i];
                }
            }
        }
    }
}
