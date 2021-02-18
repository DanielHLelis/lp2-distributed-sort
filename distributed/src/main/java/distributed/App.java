package distributed;

import br.cefetmg.parallelSort.sort.ISorter;
import br.cefetmg.parallelSort.sort.single.SingleMergeSort;

import java.util.List;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
      List<Integer> l = List.of(13, 543 , 1234, 5, -543, 4095, 432, 0, 0);

      ISorter<Integer> sorter = new SingleMergeSort<>();

      System.out.println(sorter.sort(l));



    }
}
