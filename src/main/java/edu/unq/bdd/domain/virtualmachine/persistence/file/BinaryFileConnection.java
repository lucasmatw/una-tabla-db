package edu.unq.bdd.domain.virtualmachine.persistence.file;

public interface BinaryFileConnection {

    long length();

    byte[] readAll();

    byte[] readChunkAt(int index, int size);

    void saveChunkAt(int index, byte[] chunk);
}
