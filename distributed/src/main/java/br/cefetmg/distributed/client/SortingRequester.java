package br.cefetmg.distributed.client;

import br.cefetmg.distributed.IntegerListParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SortingRequester {
  private class QueueElement {
    public List<Integer> listToSort;
    public int originalIndex;

    public QueueElement(List<Integer> listToSort, int originalIndex) {
      this.listToSort = listToSort;
      this.originalIndex = originalIndex;
    }
  }

  private class SortingWorker implements Runnable {
    private final byte SORT_REQUEST_SIGNAL = -128;
    private final byte SORT_RESPONSE_SIGNAL = 127;
    private final byte SORT_ERROR_SIGNAL = -1;

    private InetSocketAddress socketAddress;

    public SortingWorker(InetSocketAddress socketAddress) {
      this.socketAddress = socketAddress;
    }

    @Override
    public void run() {
      QueueElement element;
      try{
        while ((element = nextElement()) != null){
          Socket socket = new Socket(socketAddress.getHostName(), socketAddress.getPort());

          DataOutputStream output = new DataOutputStream(socket.getOutputStream());
          DataInputStream input = new DataInputStream(socket.getInputStream());

          // Send
          output.writeByte(SORT_REQUEST_SIGNAL);
          output.writeInt(element.listToSort.size());

          output.write(IntegerListParser.toByteArray(element.listToSort));

          // Receive
          byte signal = input.readByte();

          if(signal == SORT_ERROR_SIGNAL){
            throw new RuntimeException("Sorting failure at server: " + input.readUTF());
          }

          if(signal != SORT_RESPONSE_SIGNAL){
            throw new RuntimeException("Unknown signal: " + signal);
          }

          int length = input.readInt();

          List<Integer> resultList = IntegerListParser.fromByteArray(input.readNBytes(length*4));

          // Write result
          outputList.set(element.originalIndex, resultList);

          // Close connection
          socket.close();
        }
      } catch (IOException ex){
        throw new RuntimeException(ex);
      }

    }
  }

  private Queue<QueueElement> sortingQueue;
  private ArrayList<List<Integer>> outputList;

  private SortingRequester(List<List<Integer>> list){
    sortingQueue = new LinkedList<>();
    outputList = new ArrayList<>();
    int i = 0;
    for(var l: list){
      outputList.add(null);
      sortingQueue.add(new QueueElement(l, i));
      i++;
    }
  }

  private synchronized QueueElement nextElement(){
    return sortingQueue.poll();
  }

  private void sort(List<InetSocketAddress> workers) throws InterruptedException{
    List<Thread> runningThreads = new LinkedList<>();

    for(var w: workers){
      Thread t = new Thread(new SortingWorker(w));
      runningThreads.add(t);
      t.start();
    }

    for(var t: runningThreads){
      t.join();
    }
  }

  public static List<List<Integer>> runSort(List<List<Integer>> data, List<InetSocketAddress> workers) throws InterruptedException, IOException {
    SortingRequester requester = new SortingRequester(data);
    requester.sort(workers);
    return requester.outputList;
  }

}
