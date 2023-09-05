package edu.unq.bdd.domain.virtualmachine.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Page {
    private final byte[] bytearray;
    private int currentIndex;


    public Page(int size) {
        this.bytearray = new byte[size];
        currentIndex = 0;
    }

    public void save(byte[] byteChunk) {
        if (insufficientSpace(byteChunk.length)) {
            throw new IllegalStateException("Page sin espacio suficiente");
        }

        insertIn(currentIndex, byteChunk);
        updateIndex(byteChunk);
    }

    private byte[] read(int from, int to) {
        return Arrays.copyOfRange(bytearray, from, to);
    }

    public List<byte[]> readChunks(int chunkSize) {
        var result = new ArrayList<byte[]>();
        for (int i = 0; i + chunkSize <= currentIndex; i += chunkSize) {
            result.add(read(i, i + chunkSize));
        }
        return result;
    }

    public boolean insufficientSpace(int dataSize) {
        return bytearray.length - currentIndex < dataSize;
    }

    private void insertIn(int index, byte[] byteChunk) {
        System.arraycopy(byteChunk, 0, bytearray, index, byteChunk.length);
    }

    private void updateIndex(byte[] byteChunk) {
        currentIndex += byteChunk.length;
    }
}
