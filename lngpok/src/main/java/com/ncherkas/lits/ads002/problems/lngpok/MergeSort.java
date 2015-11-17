package com.ncherkas.lits.ads002.problems.lngpok;

import java.util.Objects;

public class MergeSort {

  private MergeSort() {}

  public static void sort(int[] array) {
    Objects.requireNonNull(array);
    int[] result = sortRecursively(array, 0, array.length - 1);
    for (int i = 0; i < array.length; i++) {
      array[i] = result[i];
    }
  }

  private static int[] sortRecursively(int[] array, int left, int right) {
    if (left == right) {
      return new int[] {array[left]};
    }

    if (right - left == 1) {
      return array[left] > array[right]
          ? new int[]{ array[right], array[left] }
          : new int[]{ array[left], array[right] };
    }

    int middle = (left + right) / 2;
    int[] leftSorted = sortRecursively(array, left, middle);
    int[] rightSorted = sortRecursively(array, middle + 1, right);

    int[] result = new int[leftSorted.length + rightSorted.length];
    for (int resultIdx = 0, leftIdx = 0, rightIdx = 0; resultIdx < result.length; resultIdx++) {
      if (leftIdx < leftSorted.length && rightIdx < rightSorted.length) {
        result[resultIdx] = leftSorted[leftIdx] > rightSorted[rightIdx]
            ? rightSorted[rightIdx++] : leftSorted[leftIdx++];
      } else if (leftIdx < leftSorted.length) {
        result[resultIdx] = leftSorted[leftIdx++];
      } else {
        result[resultIdx] = rightSorted[rightIdx++];
      }
    }
    return result;
  }
}
