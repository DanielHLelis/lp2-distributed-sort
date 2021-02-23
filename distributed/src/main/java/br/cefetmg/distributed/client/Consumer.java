package br.cefetmg.distributed.client;

import br.cefetmg.parallelSort.IO.FileInputParser;
import br.cefetmg.parallelSort.IO.FileOutputParser;
import br.cefetmg.parallelSort.IO.InputParser;
import br.cefetmg.parallelSort.IO.OutputParser;
import org.apache.commons.cli.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Consumer {

  public static void main(String[] args) {
    Options cliOptions = new Options();

    Option inputPathOption = new Option("i", "input", true, "input file path");
    Option outputPathOption = new Option("o", "output", true, "output file path");

    inputPathOption.setRequired(true);
    outputPathOption.setRequired(true);

    cliOptions.addOption(inputPathOption);
    cliOptions.addOption(outputPathOption);

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

      List<List<Integer>> sortedInput = sort(parsedInput);

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

  public static List<List<Integer>> sort(List<List<Integer>> input) throws IOException{
    List<List<Integer>> listsOut = new ArrayList<>();

    for(var l: input){
      StringBuilder bolacha = new StringBuilder();
      bolacha.append("sort\n");

      for(var i: l){
        bolacha.append(i);
        bolacha.append('\n');
      }

      Socket socket = new Socket("127.0.0.1", 19000);

      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      dos.writeUTF(bolacha.toString());

      DataInputStream dis = new DataInputStream(socket.getInputStream());

      listsOut.add(
              Arrays.stream(dis.readUTF().split("\n"))
                      .filter(e -> !e.isBlank())
                      .map(Integer::parseInt)
                      .collect(Collectors.toList())
      );
    }

    return listsOut;
  }
}
