package edu.unq.bdd.domain.virtualmachine.persistence.file;

import lombok.SneakyThrows;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDatabase implements BinaryFileConnection {

    private final String path;
    private final RandomAccessFile databaseFile;


    @SneakyThrows
    public FileDatabase(String path) {
        this.path = path;
        this.databaseFile = new RandomAccessFile(path, "rw");
    }

    @Override
    @SneakyThrows
    public long length() {
        return databaseFile.length();
    }

    @Override
    @SneakyThrows
    public byte[] readAll() {
        return Files.readAllBytes(Paths.get(path));
    }

    @SneakyThrows
    @Override
    public byte[] readChunkAt(int index, int size) {

        if (databaseFile.length() < index + size) {
            throw new IndexOutOfBoundsException(("trying to read %d bytes from index %d," +
                    " but file length is %d").formatted(size, index, databaseFile.length()));
        }

        databaseFile.seek(index);

        byte[] buffer = new byte[size];

        databaseFile.read(buffer);
        return buffer;
    }

    @SneakyThrows
    public void saveChunkAt(int index, byte[] chunk) {
        databaseFile.seek(index);
        databaseFile.write(chunk);
    }
}
