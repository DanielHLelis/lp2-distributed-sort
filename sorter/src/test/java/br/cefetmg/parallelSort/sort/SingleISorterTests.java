package br.cefetmg.parallelSort.sort;

import br.cefetmg.parallelSort.sort.single.SingleMergeSort;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SingleISorterTests {
    private final List<Integer> INPUT_REF = List.of(
            -31745, -55297, 74752, -78852, 3075, -51205, 18438, -88072, 66568, 43019,
            -95243, -54285, 95245, 52236, -90128, -94226, -75796, -4120, -97306, 47128
    );

    private final List<Integer> EXPECTED_REF = List.of(
            -97306, -95243, -94226, -90128, -88072, -78852, -75796, -55297, -54285,
            -51205, -31745, -4120, 3075, 18438, 43019, 47128, 52236, 66568, 74752, 95245
    );

    @Test
    public void mergeSort(){
        ISorter<Integer> ISorter = new SingleMergeSort<>();
        List<Integer> out = ISorter.sort(INPUT_REF);
        assertEquals(EXPECTED_REF, out);
    }
}
