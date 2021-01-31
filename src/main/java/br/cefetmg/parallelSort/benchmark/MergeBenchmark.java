package br.cefetmg.parallelSort.benchmark;

import br.cefetmg.parallelSort.IO.FileInputParser;
import br.cefetmg.parallelSort.sort.Sorter;
import br.cefetmg.parallelSort.sort.parallel.ThreadedMergeSort;
import br.cefetmg.parallelSort.sort.single.SingleMergeSort;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MergeBenchmark {
    public static void main(String[] args) {
        List<Integer> inputData;

        try {
            FileInputParser parser = new FileInputParser(MergeBenchmark.class.getResource("/inputs/large_input.txt").getFile());
            inputData = parser.parse().stream().map(Integer::parseInt).collect(Collectors.toList());
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }

        SingleMergeSort<Integer> singleMergeSort = new SingleMergeSort<>();
        ThreadedMergeSort<Integer> threadedMergeSort = new ThreadedMergeSort<>();

        System.out.printf(
                "Sorted %d elements\n" +
                "Single threaded avg: %.4fms\n" +
                "Multi threaded avg (%d workers): %.4fms\n",
                inputData.size(),
                avgTimeSorter(inputData, singleMergeSort, 10),
                threadedMergeSort.getMaxWorkers(), avgTimeSorter(inputData, threadedMergeSort, 10)
        );
    }

    private static double timeSorter(List<Integer> in, Sorter<Integer> sorter){
        long start = System.nanoTime();
        List<Integer> result = sorter.sort(in);
        long end = System.nanoTime();
        return (end - start) / 1e6;
    }

    private static double avgTimeSorter(List<Integer> in, Sorter<Integer> sorter, int runs){
        double time = 0;
        for(int i = 0; i < runs; i++){
            time += timeSorter(in, sorter);
        }
        return time/runs;
    }

    private static boolean isSorted(List<Integer> l){
        Integer prev = null;
        for(int i: l){
            if(prev == null){
                prev = i;
                continue;
            }

            if(prev > i){
                return false;
            }

            prev = i;
        }
        return true;
    }
}
