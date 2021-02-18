package br.cefetmg.parallelSort.sort;

import java.util.Comparator;
import java.util.List;

@FunctionalInterface
public interface IMultiSorter<T extends Comparable<T>> {
  default List<List<T>> sort(List<List<T>> in) {
    return sort(in, T::compareTo);
  }

  List<List<T>> sort(List<List<T>> in, Comparator<T> comparator);
}
