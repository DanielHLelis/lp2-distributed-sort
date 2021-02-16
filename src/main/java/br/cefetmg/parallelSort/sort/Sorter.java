package br.cefetmg.parallelSort.sort;

import java.util.Comparator;
import java.util.List;

@FunctionalInterface
public interface Sorter<T extends Comparable<T>> {
  default List<T> sort(List<T> in) {
    return sort(in, T::compareTo);
  }

  List<T> sort(List<T> in, Comparator<T> comparator);
}
