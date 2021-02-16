package br.cefetmg.parallelSort.sort.multisorters;

import br.cefetmg.parallelSort.sort.MultiSorter;
import br.cefetmg.parallelSort.sort.Sorter;
import br.cefetmg.parallelSort.sort.single.SingleMergeSort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BatchMultiSorter<T extends Comparable<T>> implements MultiSorter<T> {

  private final int maxWorkers;

  public BatchMultiSorter() {
    this(Runtime.getRuntime().availableProcessors());
  }


  public BatchMultiSorter(int maxWorkers) {
    this.maxWorkers = maxWorkers;
  }

  @Override
  public List<List<T>> sort(List<List<T>> data, Comparator<T> comparator) {
    ExecutorService executor = Executors.newFixedThreadPool(maxWorkers);
    List<List<T>> output = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {
      output.add(null);
      int id = i;
      executor.execute(() -> {
        Sorter<T> sorter = new SingleMergeSort<>();
        output.set(id, sorter.sort(data.get(id), comparator));
      });
    }

    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }

    return output;
  }
}
