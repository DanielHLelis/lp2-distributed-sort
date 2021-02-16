package br.cefetmg.parallelSort.IO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileInputParser extends FileParser implements InputParser {

  public FileInputParser(String path) throws IOException {
    super(path);
  }

  public FileInputParser(File file) throws IOException {
    super(file);
  }

  @Override
  public List<List<String>> parse() throws IOException {
    List<String> result;

    result = Files.lines(file.toPath()) // Open a Java Stream with each line
            .collect(Collectors.toList()); // Collect stream into a List

    List<List<String>> out = new ArrayList<>();
    List<String> current = new ArrayList<>();
    out.add(current);

    for (String s : result) {
      if (s.trim().isEmpty()) {
        current = new ArrayList<>();
        out.add(current);
        continue;
      }
      current.add(s);
    }

    return out;
  }
}
