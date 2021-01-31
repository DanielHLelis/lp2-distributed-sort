package br.cefetmg.parallelSort.IO;

import java.io.IOException;
import java.util.List;

public interface InputParser {
    public List<String> parse() throws IOException;
}
