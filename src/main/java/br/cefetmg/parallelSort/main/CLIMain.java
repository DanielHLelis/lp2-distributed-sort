package br.cefetmg.parallelSort.main;

import br.cefetmg.parallelSort.IO.FileInputParser;
import br.cefetmg.parallelSort.IO.FileOutputParser;
import br.cefetmg.parallelSort.IO.InputParser;
import br.cefetmg.parallelSort.IO.OutputParser;
import br.cefetmg.parallelSort.sort.parallel.ThreadedMergeSort;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CLIMain {

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
        } catch (ParseException ex){
            System.out.println(ex.getMessage());
            helpFormatter.printHelp("sorter", cliOptions);

            System.exit(1);
        }

        String inputPath = cmd.getOptionValue("input");
        String outputPath = cmd.getOptionValue("output");

        try{
            InputParser inputParser = new FileInputParser(inputPath);
            OutputParser outputParser = new FileOutputParser(outputPath);

            List<List<String>> input = inputParser.parse();

            System.out.println(input);

            ThreadedMergeSort<Integer> sorter = new ThreadedMergeSort<>();

            List<List<String>> output = new ArrayList<>();

            for(var l: input){
                List<Integer> lInt = l.stream().map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> sortedLInt = sorter.sort(lInt);
                output.add(sortedLInt.stream().map(Object::toString).collect(Collectors.toList()));
            }
            outputParser.parse(output);
        } catch (IOException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
