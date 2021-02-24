package br.cefetmg.distributed.client;

import br.cefetmg.parallelSort.IO.FileInputParser;
import br.cefetmg.parallelSort.IO.FileOutputParser;
import br.cefetmg.parallelSort.IO.InputParser;
import br.cefetmg.parallelSort.IO.OutputParser;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class Consumer {

  public static void main(String[] args) {
    Options cliOptions = new Options();

    Option inputPathOption = new Option("i", "input", true, "input file path");
    Option outputPathOption = new Option("o", "output", true, "output file path");
    Option workerListPathOption = new Option("w", "workers", true, "worker list file path");

    inputPathOption.setRequired(true);
    outputPathOption.setRequired(true);
    workerListPathOption.setRequired(true);

    cliOptions.addOption(inputPathOption);
    cliOptions.addOption(outputPathOption);
    cliOptions.addOption(workerListPathOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter helpFormatter = new HelpFormatter();

    CommandLine cmd = null;

    try {
      cmd = parser.parse(cliOptions, args);
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      helpFormatter.printHelp("distributed", cliOptions);

      System.exit(1);
    }

    String inputPath = cmd.getOptionValue("input");
    String outputPath = cmd.getOptionValue("output");
    String workerListPath = cmd.getOptionValue("workers");

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

      List<InetSocketAddress> workers = WorkerListReader.readList(workerListPath);

      List<List<Integer>> sortedInput = SortingRequester.runSort(parsedInput, workers);

      List<List<String>> output = sortedInput.stream()
              .map(l -> l.stream()
                      .map(Object::toString)
                      .collect(Collectors.toList())
              )
              .collect(Collectors.toList());

      outputParser.parse(output);
    } catch (IOException | InterruptedException | RuntimeException ex) {
      System.err.println("Erro: " + ex.getMessage());
      System.exit(1);
    }
  }

}
