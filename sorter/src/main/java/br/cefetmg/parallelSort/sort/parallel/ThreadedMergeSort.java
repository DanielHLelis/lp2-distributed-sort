package br.cefetmg.parallelSort.sort.parallel;

import br.cefetmg.parallelSort.sort.ISorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ThreadedMergeSort<T extends Comparable<T>> implements ISorter<T> {
  private final int minFork;
  private ForkJoinPool threadPool;

  public ThreadedMergeSort() {
    this(Runtime.getRuntime().availableProcessors());
  }

  public ThreadedMergeSort(int maxWorkers) {
    this(maxWorkers, 1 << 13);
  }

  public ThreadedMergeSort(int maxWorkers, int minFork) {
    if (maxWorkers < 1) {
      throw new IllegalArgumentException("At least 1 worker necessary");
    }
    threadPool = new ForkJoinPool(maxWorkers);
    this.minFork = minFork;
  }

  @Override
  public List<T> sort(List<T> in, Comparator<T> comparator) {
    ArrayList<T> data = new ArrayList<>(in);
    SortWorker sorterThread = new SortWorker(data, comparator, 0, in.size());
    threadPool.invoke(sorterThread);
    return data;
  }

  private class SortWorker extends RecursiveAction {
    private final ArrayList<T> data;
    private final Comparator<T> comparator;
    private final int begin;
    private final int end;
    private final int len;
    private final int mid;

    public SortWorker(ArrayList<T> data, Comparator<T> comparator, int begin, int end) {
      this.data = data;
      this.comparator = comparator;
      this.begin = begin;
      this.end = end;

      this.len = end - begin;
      this.mid = begin + this.len / 2;
    }

    public void sort(int b, int e) {
      if (e - b < 2) {
        return;
      }
      int m = b + (e - b) / 2;
      sort(b, m);
      sort(m, e);
      merge(b, m, e);
    }

    public void sort() {
      if (len < 2) {
        return;
      }

      if (len > minFork) {
        SortWorker sortWorker1 = new SortWorker(data, comparator, begin, mid);
        SortWorker sortWorker2 = new SortWorker(data, comparator, mid, end);
        invokeAll(sortWorker1, sortWorker2);
      } else {
        sort(begin, end);
      }

      merge();
    }

    private void merge() {
      merge(begin, mid, end);
    }

    private void merge(int b, int m, int e) {
      /*
       * b: begin
       * m: mid
       * e: end
       * */
      int insertIndex = b;

      ArrayList<T> leftList = new ArrayList<>(data.subList(b, m));
      ArrayList<T> rightList = new ArrayList<>(data.subList(m, e));

      int leftIndex = 0, leftSize = leftList.size();
      int rightIndex = 0, rightSize = rightList.size();

      while (leftIndex < leftSize && rightIndex < rightSize) {
        T left = leftList.get(leftIndex);
        T right = rightList.get(rightIndex);
        if (comparator.compare(left, right) <= 0) {
          leftIndex++;
          data.set(insertIndex++, left);
        } else {
          rightIndex++;
          data.set(insertIndex++, right);
        }
      }

      for (; leftIndex < leftSize; leftIndex++) {
        data.set(insertIndex++, leftList.get(leftIndex));
      }

      for (; rightIndex < rightSize; rightIndex++) {
        data.set(insertIndex++, rightList.get(rightIndex));
      }
    }


    @Override
    public void compute() {
      sort();
    }
  }
}
