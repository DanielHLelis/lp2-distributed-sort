package br.cefetmg.distributed;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerListParser {
  public static byte[] toByteArray(List<Integer> l) {
    ByteBuffer bb = ByteBuffer.allocate(l.size() * 4);
    for(var e: l){
      bb.putInt(e);
    }
    return bb.array();
  };

  public static List<Integer> fromByteArray(byte[] bytes) {
    List<Integer> result = new ArrayList<>(bytes.length/4);

    IntBuffer ib = ByteBuffer.wrap(bytes).asIntBuffer();
    ib.rewind();
    for(int i = 0; i < bytes.length/4; i++){
      result.add(ib.get());
    }

    return result;
  }

}
