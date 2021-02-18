package br.cefetmg.parallelSort.main;

import br.cefetmg.parallelSort.IO.FileInputParser;
import br.cefetmg.parallelSort.IO.FileOutputParser;
import br.cefetmg.parallelSort.IO.InputParser;
import br.cefetmg.parallelSort.IO.OutputParser;
import br.cefetmg.parallelSort.sort.IMultiSorter;
import br.cefetmg.parallelSort.sort.multisorters.BatchMultiSort;
import br.cefetmg.parallelSort.sort.multisorters.SerialMultiSort;
import br.cefetmg.parallelSort.sort.parallel.ThreadedMergeSort;
import br.cefetmg.parallelSort.sort.single.SingleMergeSort;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CLIMain {

  final static String DEFAULT_ALGORITHM = "batch";
  final static Map<String, IMultiSorter<Integer>> ALGORITHMS = new HashMap<>() {
    {
      put("serial", new SerialMultiSort<Integer>(new SingleMergeSort<>()));
      put("parallel", new SerialMultiSort<Integer>(new ThreadedMergeSort<>()));
      put("batch", new BatchMultiSort<>());
    }
  };

  public static void main(String[] args) {
    Options cliOptions = new Options();

    Option inputPathOption = new Option("i", "input", true, "input file path");
    Option outputPathOption = new Option("o", "output", true, "output file path");
    Option algorithmPathOption = new Option("a", "algorithm", true, "algorithm to be used [serial, parallel, batch]");

    inputPathOption.setRequired(true);
    outputPathOption.setRequired(true);
    algorithmPathOption.setOptionalArg(true);

    cliOptions.addOption(inputPathOption);
    cliOptions.addOption(outputPathOption);
    cliOptions.addOption(algorithmPathOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter helpFormatter = new HelpFormatter();

    CommandLine cmd = null;

    try {
      cmd = parser.parse(cliOptions, args);
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      helpFormatter.printHelp("sorter", cliOptions);

      System.exit(1);
    }

    String inputPath = cmd.getOptionValue("input");
    String outputPath = cmd.getOptionValue("output");
    String algorithm = cmd.getOptionValue("algorithm");


    if (algorithm == null) {
      algorithm = DEFAULT_ALGORITHM;
    }

    if (!ALGORITHMS.containsKey(algorithm)) {
      System.err.printf("Algorithm (%s) not found, defaulting to (%s)\n", algorithm, DEFAULT_ALGORITHM);
      algorithm = DEFAULT_ALGORITHM;
    }

    try {
      InputParser inputParser = new FileInputParser(inputPath);
      OutputParser outputParser = new FileOutputParser(outputPath);

      List<List<String>> input = inputParser.parse();

      List<List<Integer>> parsedInput = input.stream()
              .map(l -> l.stream()
                      .map(Integer::parseInt)
                      .collect(Collectors.toList())
              )
              .collect(Collectors.toList());

      IMultiSorter<Integer> multiSorter = ALGORITHMS.get(algorithm);

      System.out.printf("Sorting with algorithm: %s\n", algorithm);

      List<List<Integer>> sortedInput = multiSorter.sort(parsedInput);

      List<List<String>> output = sortedInput.stream()
              .map(l -> l.stream()
                      .map(Object::toString)
                      .collect(Collectors.toList())
              )
              .collect(Collectors.toList());

      outputParser.parse(output);
    } catch (IOException ex) {
      System.err.println("Erro: " + ex.getMessage());
      System.exit(1);
    }
  }
}
