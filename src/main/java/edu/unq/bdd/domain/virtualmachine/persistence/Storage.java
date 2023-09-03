package edu.unq.bdd.domain.virtualmachine.persistence;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Storage<T> {

    private final int pageSize;
    private final LinkedList<Page> pages;

    private int records;

    public Storage(int pageSize) {
        this.pageSize = pageSize;
        pages = new LinkedList<>();
        pages.add(new Page(pageSize));
        records = 0;
    }

    public void save(T entity) {
        byte[] serialized = serialize(entity);

        ensureSpace(serialized.length);
        pages.getLast().save(serialized);
        increaseRecords();
    }

    public List<T> getAll() {
        return pages.stream()
                .map(p -> p.readChunks(recordSize()))
                .flatMap(Collection::stream)
                .map(this::deserialize)
                .collect(Collectors.toList());
    }

    public Metadata getMetadata() {
        return new Metadata(pages.size(), records);
    }

    protected abstract T deserialize(byte[] bytes);

    protected abstract byte[] serialize(T entity);
    protected abstract int recordSize();
    private void increaseRecords() {
        records ++;
    }

    private void ensureSpace(int size) {
        if (pages.getLast().insufficientSpace(size)) {
            pages.add(new Page(pageSize));
        }
    }
}
