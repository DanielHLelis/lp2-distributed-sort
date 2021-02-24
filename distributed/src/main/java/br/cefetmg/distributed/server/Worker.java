package br.cefetmg.distributed.server;

import br.cefetmg.distributed.IntegerListParser;
import br.cefetmg.parallelSort.sort.ISorter;
import br.cefetmg.parallelSort.sort.parallel.ThreadedMergeSort;
import br.cefetmg.parallelSort.sort.single.SingleMergeSort;
import org.apache.commons.cli.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Worker {

  private static ISorter<Integer> sorter;


  public static void sort(Socket socket) throws IOException {
    System.out.println("Sort payload");

    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

    try {
      byte flag = inputStream.readByte();

      if (flag != -128) {
        throw new Exception("Invalid operation");
      }

      int listSize = inputStream.readInt();

      byte[] bytes = inputStream.readNBytes(listSize * 4);

      List<Integer> parsedData = IntegerListParser.fromByteArray(bytes);

      System.out.printf("Sorting integer list of size %d\n", parsedData.size());

      List<Integer> sortedData = sorter.sort(parsedData);

      outputStream.writeByte(127);
      outputStream.writeInt(sortedData.size());

      outputStream.write(IntegerListParser.toByteArray(sortedData));

      outputStream.flush();
    } catch (Exception ex) {
      System.out.println("Exception: " + ex.getMessage());
      outputStream.writeByte(-1);
      outputStream.writeUTF(ex.getMessage());
      outputStream.flush();
    }
  }

  public static void serve(int port, int backlog, String host, int workers) throws IOException {
    InetAddress addr = InetAddress.getByName(host);
    ServerSocket serverSocket = new ServerSocket(port, backlog, addr);

    System.out.printf("Server listening at %s:%d\n", host, port);

    for(int i = 0; i < workers; i++){
      new Thread(() -> {
        try{
          handleConnection(serverSocket);
        }catch (IOException ex){
          throw new RuntimeException(ex);
        }
      }).start();
    }
  }

  public static void handleConnection(ServerSocket serverSocket) throws IOException{
    System.out.println("New worker running");
    while (true) {
      Socket socket = serverSocket.accept();
      System.out.printf("Received request at %s\n", socket.getInetAddress().toString());

      try {
        sort(socket);
      } catch (Exception ex) {
        System.out.println("Exception: " + ex.getMessage());
      }

      System.out.printf("Closing request at %s\n", socket.getInetAddress().toString());

      socket.close();
    }
  }

  public static void main(String[] args) {
    Options cliOptions = new Options();

    Option portOption = new Option("p", "port", true, "server port (default: 19000)");
    Option hostOption = new Option("h", "host", true, "server host (default: 0.0.0.0)");
    Option workersOption = new Option("w", "workers", true, "threads used for sorting (default :1)");

    portOption.setRequired(false);
    hostOption.setRequired(false);
    workersOption.setRequired(false);

    cliOptions.addOption(portOption);
    cliOptions.addOption(hostOption);
    cliOptions.addOption(workersOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter helpFormatter = new HelpFormatter();

    CommandLine cmd = null;

    try {
      cmd = parser.parse(cliOptions, args);
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      helpFormatter.printHelp("distributed", cliOptions);

      System.exit(1);
    }

    String portArg = cmd.getOptionValue("port");
    String hostArg = cmd.getOptionValue("host");
    String workersArg = cmd.getOptionValue("workersArg");

    if (portArg == null) {
      portArg = "19000";
    }

    if (hostArg == null) {
      hostArg = "0.0.0.0";
    }

    if (workersArg == null) {
      workersArg = "1";
    }

    int port;
    int workers;

    try {
      port = Integer.parseInt(portArg);
      workers = Integer.parseInt(workersArg);
      sorter = new SingleMergeSort<>();
      Worker.serve(port, 50, hostArg, workers);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
