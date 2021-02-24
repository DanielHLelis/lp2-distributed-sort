package br.cefetmg.distributed.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class WorkerListReaderTest {

  @Test
  public void TestReading() throws IOException {
    String filePath = this.getClass().getResource("/test_worker_list.txt").getPath();
    List<InetSocketAddress> addresses =  WorkerListReader.readList(filePath);

    List<InetSocketAddress> expected = List.of(
      new InetSocketAddress("127.0.0.1", 19000),
      new InetSocketAddress("127.0.0.1", 19001),
      new InetSocketAddress("127.0.0.1", 19002),
      new InetSocketAddress("127.0.0.1", 19003),
      new InetSocketAddress("127.0.0.1", 19004)
    );

    assertIterableEquals(expected, addresses);
  }
}
