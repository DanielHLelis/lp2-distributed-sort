package br.cefetmg.distributed.server;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Worker {

  public static void sort(Socket socket, int workers) throws IOException {
    System.out.println("Sort payload");

    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

    String data = inputStream.readUTF();
    String[] rows = data.split("\n");

    try {
      if (rows[0].equals("sort")) {
        List<String> clearedData = new ArrayList<>(Arrays.asList(rows));
        clearedData.remove(0);

        List<Integer> parsedData = clearedData.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.printf("Sorting integer list of size %d\n", parsedData.size());

        ISorter<Integer> sorter = new SingleMergeSort<>();
        List<Integer> sortedData = sorter.sort(parsedData);

        StringBuilder outputData = new StringBuilder();

        for (var l : sortedData) {
          outputData.append(l.toString());
          outputData.append('\n');
        }

        outputStream.writeUTF(outputData.toString());
        outputStream.flush();
      }else {
        throw new Exception();
      }
    } catch (Exception ex) {
      System.out.println("Exception: " + ex.getMessage());
      outputStream.writeUTF("error\n");
      outputStream.flush();
    }
  }

  public static void serve(int port, int backlog, String host, int workers) throws IOException {
    InetAddress addr = InetAddress.getByName(host);
    ServerSocket serverSocket = new ServerSocket(port, backlog, addr);

    System.out.printf("Server listening at %s:%d\n", host, port);

    while (true) {
      Socket socket = serverSocket.accept();
      System.out.printf("Received request at %s\n", socket.getInetAddress().toString());

      try{
        sort(socket, workers);
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

    if(portArg == null){
      portArg = "19000";
    }

    if(hostArg == null){
      hostArg = "0.0.0.0";
    }

    if(workersArg == null){
      workersArg = "1";
    }

    int port;
    int workers;

    try{
      port = Integer.parseInt(portArg);
      workers = Integer.parseInt(workersArg);
      Worker.serve(port,50, hostArg, workers);
    }catch (Exception e){
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
