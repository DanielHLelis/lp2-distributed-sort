package br.cefetmg.parallelSort.IO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileInputParser implements InputParser{

    private final File file;

    public FileInputParser(String path) throws IOException{
        this(new File(path));
    }

    public FileInputParser(File file) throws IOException{
        if (file == null){
            throw new NullPointerException("file can't be null");
        }

        if(!file.isFile()){
            throw new IOException("Invalid file");
        }

        this.file = file;
    }

    @Override
    public List<String> parse() throws IOException {
        List<String> result;

        result = Files.lines(file.toPath()) // Open a Java Stream with each line
                .filter(s -> !s.isEmpty()) // Ignore empty lines
                .collect(Collectors.toList()); // Collect stream into a List

        return result;
    }
}
