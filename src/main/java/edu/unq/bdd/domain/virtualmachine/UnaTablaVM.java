package edu.unq.bdd.domain.virtualmachine;

import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.bytecode.Operation;
import edu.unq.bdd.domain.model.Person;
import edu.unq.bdd.domain.virtualmachine.persistence.Metadata;
import edu.unq.bdd.domain.virtualmachine.persistence.Storage;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UnaTablaVM implements VirtualMachine {

    private static final int MAIN_TABLE_FIELDS = 3;

    private final Storage<Person> personStorage;

    @Override
    public ExecutionResult run(ByteCode byteCode) {
        ExecutionState finalState = byteCode.getOperations().stream()
                .reduce(ExecutionState.empty(), (state, op) -> executeOperation(op, state),
                        (s1, s2) -> s2);
        return buildResult(finalState);
    }

    private ExecutionResult buildResult(ExecutionState state) {
        String data = String.join("\n", state.getResult());
        return new ExecutionResult(data, state.isTerminate());
    }

    private ExecutionState executeOperation(Operation operation, ExecutionState state) {
        return switch (operation.getOpCode()) {
            case EXIT -> executeExit();
            case SELECT -> executeSelect();
            case INSERT -> executeInsert(operation);
            case METADATA -> executeMetadata();
            default -> executeInvalidCommand();
        };
    }

    private ExecutionState executeMetadata() {
        Metadata metadata = personStorage.getMetadata();
        String prettyPages = "Paginas: " + metadata.pages();
        String prettyRecords = "Registros: " + metadata.records();

        return ExecutionState.withResults(List.of(prettyPages, prettyRecords));
    }

    private ExecutionState executeSelect() {
        List<String> results = personStorage.getAll()
                .stream()
                .map(this::rowString)
                .collect(Collectors.toList());

        return ExecutionState.withResults(results);
    }

    private String rowString(Person person) {
        return String.format("%s %s %s", person.id(), person.usuario(), person.email());
    }

    private ExecutionState executeInsert(Operation operation) {

        if (isInvalidOperation(operation)) {
            return ExecutionState.withResult("Operación inválida");
        }

        List<String> fields = operation.getFields();

        int id = Integer.parseInt(fields.get(0));
        String user = fields.get(1);
        String email = fields.get(2);
        Person person = new Person(id, user, email);

        personStorage.save(person);

        return ExecutionState.withResult("INSERT exitoso");
    }

    private boolean isInvalidOperation(Operation operation) {
        return operation.getFields().size() != MAIN_TABLE_FIELDS;
    }

    private ExecutionState executeInvalidCommand() {
        return ExecutionState.withResult("Comando inválido");
    }

    private ExecutionState executeExit() {
        return ExecutionState.terminate("Terminado");
    }
}
