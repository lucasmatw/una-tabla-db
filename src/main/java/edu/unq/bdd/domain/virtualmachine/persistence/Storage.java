package edu.unq.bdd.domain.virtualmachine.persistence;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Storage<T> {

    private final Pager pager;


    public Storage(Pager pager) {
        this.pager = pager;
    }

    public void save(T entity) {
        byte[] serialized = serialize(entity);
        getLastPage().save(serialized);
    }

    private Page getLastPage() {
        int pageId = pager.pageCount() == 0 ? 0 : pager.pageCount() - 1;
        Page page = pager.get(pageId);
        if (page.isFull()) {
            page = pager.get(pageId + 1);
        }
        return page;
    }

    public List<T> getAll() {
        return IntStream.range(0, pager.pageCount())
                .mapToObj(pager::get)
                .map(page -> page.readChunks(recordSize()))
                .flatMap(Collection::stream)
                .map(this::deserialize)
                .collect(Collectors.toList());
    }

    public Metadata getMetadata() {
        return new Metadata(pager.pageCount(), pager.recordCount());
    }

    protected abstract T deserialize(byte[] bytes);

    protected abstract byte[] serialize(T entity);

    protected abstract int recordSize();

    public void persist() {
        this.pager.persist();
    }
}
