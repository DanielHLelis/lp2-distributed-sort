package br.cefetmg.parallelSort.sort.single;

import br.cefetmg.parallelSort.sort.Sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SingleMergeSort<T extends Comparable<T>> implements Sorter<T> {

  private void _sort(ArrayList<T> ref, Comparator<T> comparator, int begin, int end) {
    // begin: inclusive
    // end: exclusive

    int len = end - begin;

    if (len < 2) {
      return;
    }

    int mid = begin + len / 2;

    _sort(ref, comparator, begin, mid);
    _sort(ref, comparator, mid, end);

    // Merge
    merge(ref, comparator, begin, end);
  }

  public void merge(ArrayList<T> ref, Comparator<T> comparator, int begin, int end) {
    int len = end - begin;
    int mid = begin + len / 2;

    ArrayList<T> buffer = new ArrayList<>(len);

    int left_index = begin;
    int right_index = mid;

    while (left_index < mid && right_index < end) {
      T left = ref.get(left_index);
      T right = ref.get(right_index);
      if (comparator.compare(left, right) <= 0) {
        buffer.add(left);
        left_index += 1;
      } else {
        buffer.add(right);
        right_index += 1;
      }
    }

    while (left_index < mid) {
      buffer.add(ref.get(left_index));
      left_index += 1;
    }

    while (right_index < end) {
      buffer.add(ref.get(right_index));
      right_index += 1;
    }

    for (int i = 0; i < len; i++) {
      ref.set(begin + i, buffer.get(i));
    }
  }

  @Override
  public List<T> sort(List<T> in, Comparator<T> comparator) {
    ArrayList<T> data = new ArrayList<>(in);

    _sort(data, comparator, 0, data.size());

    return data;
  }
}
