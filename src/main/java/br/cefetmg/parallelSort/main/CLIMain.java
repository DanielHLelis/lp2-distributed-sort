package br.cefetmg.parallelSort.main;

import br.cefetmg.parallelSort.IO.FileInputParser;
import br.cefetmg.parallelSort.IO.FileOutputParser;
import br.cefetmg.parallelSort.IO.InputParser;
import br.cefetmg.parallelSort.IO.OutputParser;
import br.cefetmg.parallelSort.sort.parallel.ThreadedMergeSort;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;

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

            List<String> input = inputParser.parse();
            ThreadedMergeSort<String> sorter = new ThreadedMergeSort<>();
            List<String> output = sorter.sort(input);
            outputParser.parse(output);
        } catch (IOException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
