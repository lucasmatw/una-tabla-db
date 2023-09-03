package edu.unq.bdd.domain.model;

import at.favre.lib.bytes.Bytes;
import edu.unq.bdd.domain.virtualmachine.persistence.Storage;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonStorage extends Storage<Person> {

    private static final int RECORD_SIZE = 291;

    protected static final int USUARIO_FIELD_SIZE = 32;
    protected static final int EMAIL_FIELD_SIZE = 253;
    protected static final int ID_FIELD_SIZE = 4;

    private static final byte NUL_DELIMITER = 0;

    public PersonStorage(int pageSize) {
        super(pageSize);
    }

    @Override
    protected Person deserialize(byte[] bytes) {

        AtomicInteger offset = new AtomicInteger(0);
        Integer id = deserializeInt(bytes, offset.getAndAdd(ID_FIELD_SIZE), ID_FIELD_SIZE);
        String user = deserializeVarChar(bytes, offset.getAndAdd(USUARIO_FIELD_SIZE), USUARIO_FIELD_SIZE);
        String email = deserializeVarChar(bytes, offset.get(), EMAIL_FIELD_SIZE);
        return new Person(id, user, email);
    }

    private Integer deserializeInt(byte[] bytes, int from, int to) {
        return Bytes.from(bytes).copy(from, to).toInt();
    }

    @SneakyThrows
    private String deserializeVarChar(byte[] bytes, int from, int to) {
        Byte[] byteResult = Bytes.from(bytes).copy(from, to)
                .toList()
                .stream()
                .takeWhile(b -> b != NUL_DELIMITER)
                .toArray(Byte[]::new);

        return new String(Bytes.from(byteResult).array(), StandardCharsets.UTF_8);
    }

    @Override
    protected byte[] serialize(Person entity) {

        byte[] id = serializeInt(entity.id());
        byte[] usuario = serializeStringInLength(entity.usuario(), USUARIO_FIELD_SIZE);
        byte[] email = serializeStringInLength(entity.email(), EMAIL_FIELD_SIZE);

        var record = new byte[RECORD_SIZE];

        AtomicInteger offset = new AtomicInteger(0);
        System.arraycopy(id, 0, record, newOffset(offset, ID_FIELD_SIZE), id.length);
        System.arraycopy(usuario, 0, record, newOffset(offset, USUARIO_FIELD_SIZE), usuario.length);
        System.arraycopy(email, 0, record, newOffset(offset, EMAIL_FIELD_SIZE), email.length);

        return record;
    }

    @Override
    protected int recordSize() {
        return RECORD_SIZE;
    }


    private int newOffset(AtomicInteger current, int dataLength) {
        return current.getAndAdd(dataLength);
    }

    private byte[] serializeInt(int i) {
        return Bytes.from(i).array();
//        return new byte[]{(byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) (i & 0xFF)};
    }

    private byte[] serializeStringInLength(String str, int length) {
        byte[] strBytes = str.getBytes(StandardCharsets.US_ASCII);
        var dataLength = Math.min(strBytes.length, length);
        return Bytes.from(str).copy(0, dataLength).append(NUL_DELIMITER).array();
    }
}
