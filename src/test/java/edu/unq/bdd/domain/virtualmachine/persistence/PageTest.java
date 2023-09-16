package edu.unq.bdd.domain.virtualmachine.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageTest {

    @Test
    @DisplayName("despues de guardar 2 bytes, y leer chunk de 2 bytes, deberia retornar solo un chunk")
    void testReadChunks() {
        // given
        Page page = new Page(10, 10);

        byte[] bytes = new byte[]{1, 1};

        page.save(bytes);

        // when
        List<byte[]> result = page.readChunks(2);

        // then
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("despues de guardar 4 bytes, y leer chunk de 2 bytes, deberia retornar 2 chunks")
    void testReadTwoChunks() {
        // given
        Page page = new Page(10, 10);

        byte[] bytes = new byte[]{1, 1};

        page.save(bytes);
        page.save(bytes);

        // when
        List<byte[]> result = page.readChunks(2);

        // then
        assertEquals(2, result.size());
    }
}