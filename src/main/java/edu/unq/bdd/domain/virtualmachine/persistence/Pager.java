package edu.unq.bdd.domain.virtualmachine.persistence;

import edu.unq.bdd.domain.virtualmachine.persistence.file.BinaryFileConnection;

import java.util.Map;

public class Pager {

    // TODO mover
    private final int recordSize;
    private final int pageSize;
    private final BinaryFileConnection binaryFileConnection;
    private final Map<Integer, Page> pageCache;
    private int pageCount;
    private int recordCount;

    public Pager(int recordSize, int pageSize, BinaryFileConnection binaryFileConnection, Map<Integer, Page> pageCache) {
        this.recordSize = recordSize;
        this.pageSize = pageSize;
        this.binaryFileConnection = binaryFileConnection;
        this.pageCache = pageCache;
        this.init();
    }

    private void init() {

        int modPage = (int) (binaryFileConnection.length() % pageSize);
        int divPageCount = (int) (binaryFileConnection.length() / pageSize);

        this.pageCount = modPage == 0 ? divPageCount : divPageCount + 1;

        this.recordCount = getRecordCount(modPage, calculateRecordsPerPage());
    }

    private int getRecordCount(int modPage, int recordsPerPage) {
        int lastPageRecords = modPage == 0 ? 0 : modPage / recordSize;
        int fullPagesRecords = pageCount == 0 ? 0 : (pageCount - 1) * recordsPerPage;
        return fullPagesRecords + lastPageRecords;
    }

    public int pageCount() {
        return pageCount;
    }

    public int recordCount() {
        return recordCount + getUnsavedRecords();
    }

    private int getUnsavedRecords() {
        return pageCache.values().stream().mapToInt(Page::getUnsavedRecords).sum();
    }

    public void persist() {
        pageCache.forEach((id, page) ->
                binaryFileConnection.saveChunkAt(id * pageSize, page.getBytearray()));
    }

    public Page get(int pageId) {
        Page page = pageCache.get(pageId);
        if (page == null) {
            page = getUnachedPage(pageId);
            pageCache.put(pageId, page);
        }

        return page;
    }

    private Page getUnachedPage(int pageId) {
        if (pageExists(pageId)) {
            return loadPage(pageId);
        }
        return createPage();
    }

    private boolean pageExists(int pageId) {
        return pageId < pageCount;
    }

    private Page loadPage(int pageId) {
        return new Page(recordSize, pageSize, readPage(pageId));
    }

    private Page createPage() {
        pageCount++;
        return new Page(recordSize, pageSize);
    }

    private byte[] readPage(int pageId) {

        int chunkToRead = pageSize;
        if (isLastPage(pageId)) {
            chunkToRead = (recordCount() % calculateRecordsPerPage()) * recordSize;
        }

        return binaryFileConnection.readChunkAt(pageId * pageSize, chunkToRead);
    }

    private boolean isLastPage(int pageId) {
        return pageId == pageCount - 1;
    }

    private int calculateRecordsPerPage() {
        return pageSize / recordSize;
    }
}
