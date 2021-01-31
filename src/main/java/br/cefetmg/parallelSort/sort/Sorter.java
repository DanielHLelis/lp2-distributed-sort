package br.cefetmg.parallelSort.sort;

import java.util.Comparator;
import java.util.List;

public interface Sorter<T extends Comparable<T>>{
    public List<T> sort(List<T> in);
    public List<T> sort(List<T> in, Comparator<T> comparator);
}
