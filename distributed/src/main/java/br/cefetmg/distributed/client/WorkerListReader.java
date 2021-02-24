package br.cefetmg.distributed.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerListReader {
  public static List<InetSocketAddress> readList(String filePath) throws IOException {
    File file = new File(filePath);
    return Files.lines(file.toPath()).filter(l -> !l.isEmpty()).map(l -> {
      String[] parts = l.split(":");
      if (parts.length != 2){
        throw new RuntimeException("Invalid workers file");
      }

      return new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
    }).collect(Collectors.toList());
  }
}
