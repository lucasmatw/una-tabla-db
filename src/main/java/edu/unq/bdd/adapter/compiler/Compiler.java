package edu.unq.bdd.adapter.compiler;

import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.bytecode.Operation;
import edu.unq.bdd.domain.bytecode.OperationFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Compiler implements CommandCompiler {

    private static final String SELECT_CMD = "select";
    private static final String INSERT_CMD_PREFIX = "insert";
    private static final String EXIT_CMD = ".exit";
    private static final String METADATA_CMD = ".table-metadata";

    @Override
    public ByteCode parse(String input) {
        return ByteCode.builder()
                .operations(parseOperations(input))
                .build();
    }

    private List<Operation> parseOperations(String input) {
        if(input.startsWith(SELECT_CMD)){
            return parseSelect();
        } else if (input.startsWith(INSERT_CMD_PREFIX)) {
            return parseInsert(input);
        } else if (input.equals(EXIT_CMD)) {
            return parseExit();
        } else if (input.equals(METADATA_CMD)){
            return parseMetadata();
        } else {
            return unrecognizedCommand();
        }
    }

    private List<Operation> parseMetadata() {
        return List.of(OperationFactory.buildMetadataCommand());
    }

    private List<Operation> unrecognizedCommand() {
        return List.of(OperationFactory.buildInvalidCommand());
    }

    private List<Operation> parseSelect() {
        return List.of(OperationFactory.buildSelect());
    }

    private List<Operation> parseInsert(String input) {
        String[] spaceSplit = input.split(" ");
        List<String> fields = Stream.of(spaceSplit)
                .skip(1)
                .collect(Collectors.toList());
        return List.of(OperationFactory.buildInsert(fields));
    }

    private List<Operation> parseExit() {
        return List.of(OperationFactory.buildExit());
    }
}
