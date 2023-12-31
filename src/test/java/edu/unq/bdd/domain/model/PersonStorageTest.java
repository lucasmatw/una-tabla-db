package edu.unq.bdd.domain.model;

import edu.unq.bdd.domain.virtualmachine.persistence.Metadata;
import edu.unq.bdd.domain.virtualmachine.persistence.Pager;
import edu.unq.bdd.domain.virtualmachine.persistence.file.BinaryFileConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static edu.unq.bdd.domain.model.PersonStorage.ID_FIELD_SIZE;
import static edu.unq.bdd.domain.model.PersonStorage.USUARIO_FIELD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PersonStorageTest {

    @Test
    @DisplayName("Serializar Person a bytes")
    void serializeOk() {
        // given
        PersonStorage personStorage = new PersonStorage(null);

        Person person = new Person(100, "a", "e");

        // when
        byte[] serialize = personStorage.serialize(person);

        // then
        assertEquals(100, serialize[3]);
        assertEquals(97, serialize[ID_FIELD_SIZE]);
        assertEquals(101, serialize[ID_FIELD_SIZE + USUARIO_FIELD_SIZE]);
    }

    @Test
    @DisplayName("Serializar y deserializar una Person")
    void serializeAndDeserializeOk() {
        // given
        PersonStorage personStorage = new PersonStorage(null);

        Person person = new Person(5000, "lucas", "lucas@unq.com");

        // when
        byte[] serialize = personStorage.serialize(person);
        Person deserializedPerson = personStorage.deserialize(serialize);

        // then
        assertEquals(person, deserializedPerson);
    }

    @Test
    @DisplayName("Sumar una Page si corresponde")
    void increasePage() {
        // given
        BinaryFileConnection binaryFileConnection = mock(BinaryFileConnection.class);
        Pager pager = new Pager(PersonStorage.RECORD_SIZE, 500, binaryFileConnection, new HashMap<>());
        PersonStorage personStorage = new PersonStorage(pager);

        Person person = new Person(1, "a", "e");

        // when
        personStorage.save(person);
        personStorage.save(person);

        // then
        Metadata metadata = personStorage.getMetadata();
        assertEquals(2, metadata.pages(), "invalid pages");
        assertEquals(2, metadata.records(), "invalid records");
    }

    @Test
    @DisplayName("Sumar 3 Pages")
    void increaseThreePage() {
        // given
        BinaryFileConnection binaryFileConnection = mock(BinaryFileConnection.class);
        Pager pager = new Pager(PersonStorage.RECORD_SIZE, 500, binaryFileConnection, new HashMap<>());
        PersonStorage personStorage = new PersonStorage(pager);

        Person person = new Person(1, "a", "e");

        // when
        personStorage.save(person);
        personStorage.save(person);
        personStorage.save(person);
        personStorage.save(person);

        // then
        Metadata metadata = personStorage.getMetadata();
        assertEquals(4, metadata.pages(), "invalid pages");
        assertEquals(4, metadata.records(), "invalid records");
    }
}