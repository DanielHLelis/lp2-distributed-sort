package br.cefetmg.parallelSort.IO;

import java.io.IOException;
import java.util.List;

public interface OutputParser {
  void parse(List<List<String>> data) throws IOException;
}
