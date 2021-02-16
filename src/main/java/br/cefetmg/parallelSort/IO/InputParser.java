package br.cefetmg.parallelSort.IO;

import java.io.IOException;
import java.util.List;

public interface InputParser {
  List<List<String>> parse() throws IOException;
}
