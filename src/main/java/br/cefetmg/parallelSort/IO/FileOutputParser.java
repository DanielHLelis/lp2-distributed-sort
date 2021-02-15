package br.cefetmg.parallelSort.IO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileOutputParser extends FileParser implements OutputParser{

    public FileOutputParser(String path) throws IOException {
        super(path);
    }

    public FileOutputParser(File file) throws IOException {
        super(file);
    }

    @Override
    public void parse(List<List<String>> data) throws IOException {
        List<String> preParsedData = data
                .stream()
                .map(ls -> String.join("\n", ls))
                .collect(Collectors.toList());
        Files.writeString(
                file.toPath(), // File path
                String.join("\n\n", preParsedData) // String list
        );
    }
}
