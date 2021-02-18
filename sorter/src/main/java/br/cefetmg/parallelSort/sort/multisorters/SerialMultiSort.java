package br.cefetmg.parallelSort.sort.multisorters;

import br.cefetmg.parallelSort.sort.IMultiSorter;
import br.cefetmg.parallelSort.sort.ISorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SerialMultiSort<T extends Comparable<T>> implements IMultiSorter<T> {
  final private ISorter<T> ISorter;

  public SerialMultiSort(ISorter<T> ISorter) {
    this.ISorter = ISorter;
  }

  @Override
  public List<List<T>> sort(List<List<T>> in, Comparator<T> comparator) {
    List<List<T>> output = new ArrayList<>();
    for (var l : in) {
      List<T> sortedList = ISorter.sort(l);
      output.add(sortedList);
    }
    return output;
  }
}
