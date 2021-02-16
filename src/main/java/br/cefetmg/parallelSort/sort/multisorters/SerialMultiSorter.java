package br.cefetmg.parallelSort.sort.multisorters;

import br.cefetmg.parallelSort.sort.MultiSorter;
import br.cefetmg.parallelSort.sort.Sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SerialMultiSorter<T extends Comparable<T>> implements MultiSorter<T> {
  final private Sorter<T> sorter;

  public SerialMultiSorter(Sorter<T> sorter) {
    this.sorter = sorter;
  }

  @Override
  public List<List<T>> sort(List<List<T>> in, Comparator<T> comparator) {
    List<List<T>> output = new ArrayList<>();
    for (var l : in) {
      List<T> sortedList = sorter.sort(l);
      output.add(sortedList);
    }
    return output;
  }
}
