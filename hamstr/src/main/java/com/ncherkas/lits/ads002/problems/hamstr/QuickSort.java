package com.ncherkas.lits.ads002.problems.hamstr;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class QuickSort {

  private QuickSort() {
  }

  public static <T> void sort(List<T> items, Comparator<T> comparator) {
    Objects.requireNonNull(items);
    Objects.requireNonNull(comparator);

    Collections.shuffle(items);

    T pivot = getPivot(items);

    int leftIndex = 0;
    int rightIndex = items.size() - 1;

    while (leftIndex <= rightIndex) {
      while (comparator.compare(pivot, items.get(leftIndex)) > 0) {
        leftIndex++;
      }

      while (comparator.compare(items.get(rightIndex), pivot) > 0) {
        rightIndex--;
      }

      if (leftIndex <= rightIndex) {
        Collections.swap(items, leftIndex, rightIndex);
        leftIndex++;
        rightIndex--;
      }
    }

    if (leftIndex - 1 > 0) {
      sort(items.subList(0, leftIndex), comparator);
    }

    if (leftIndex < items.size() - 1) {
      sort(items.subList(leftIndex, items.size()), comparator);
    }
  }

  private static <T> T getPivot(List<T> items) {
    return items.get(0);
  }
}
