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
    public void parse(List<String> data) throws IOException {
        Files.write(
                file.toPath(), // File path
                data // String list
        );
    }
}
