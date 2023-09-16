package edu.unq.bdd.domain.virtualmachine.persistence;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Page {
    // TODO mover
    private static final int ID_SIZE = 4;
    private final int pageSize;
    private final int recordSize;
    private byte[] bytearray;
    private int currentIndex;
    private int unsavedRecords;


    public Page(int recordSize, int pageSize) {
        this.recordSize = recordSize;
        this.pageSize = pageSize;
        this.bytearray = new byte[0];
        currentIndex = 0;
    }

    public Page(int recordSize, int pageSize, byte[] bytearray) {
        this.recordSize = recordSize;
        this.pageSize = pageSize;
        this.bytearray = bytearray;
        currentIndex = findIndex(bytearray);
    }

    private int findIndex(byte[] bytearray) {
        for (int i = ID_SIZE; i < bytearray.length; i = i + recordSize) {
            if (bytearray[i] == 0) {
                return i - ID_SIZE;
            }
        }
        return bytearray.length;
    }

    public void save(byte[] byteChunk) {
        if (insufficientSpace(byteChunk.length)) {
            throw new IllegalStateException("Page sin espacio suficiente");
        }

        insertIn(currentIndex, byteChunk);
        updateIndex(byteChunk);
        updateUnsavedRecords();
    }

    private void updateUnsavedRecords() {
        this.unsavedRecords++;
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
        return pageSize - currentIndex < dataSize;
    }

    private void insertIn(int index, byte[] byteChunk) {
        if (bytearray.length < index + byteChunk.length) {
            bytearray = Arrays.copyOf(bytearray, index + byteChunk.length);
        }

        System.arraycopy(byteChunk, 0, bytearray, index, byteChunk.length);
    }

    private void updateIndex(byte[] byteChunk) {
        currentIndex += byteChunk.length;
    }

    public boolean isFull() {
        return currentIndex == pageSize || currentIndex + recordSize > pageSize;
    }
}
