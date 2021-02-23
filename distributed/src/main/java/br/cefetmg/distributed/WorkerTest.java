package br.cefetmg.distributed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class WorkerTest {

  public static void main(String[] args) {
    try{
      Socket socket = new Socket("127.0.0.1", 19000);

      DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
      DataInputStream inputStream = new DataInputStream(socket.getInputStream());

      outputStream.writeUTF("sort\n" +
              "13\n" +
              "2\n" +
              "543\n" +
              "096\n" +
              "-5\n" +
              "-45\n");

      outputStream.flush();

      System.out.println("Saída:\n" + inputStream.readUTF());

      socket.close();
    } catch (Exception ex){}
  }

}
