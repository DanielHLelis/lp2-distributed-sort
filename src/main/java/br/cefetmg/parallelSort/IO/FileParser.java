package br.cefetmg.parallelSort.IO;

import java.io.File;
import java.io.IOException;

public abstract class FileParser {
    protected final File file;

    public FileParser(String path) throws IOException {
        this(new File(path));
    }

    public FileParser(File file) throws IOException{
        if (file == null){
            throw new NullPointerException("file can't be null");
        }

        this.file = file;
    }
}
