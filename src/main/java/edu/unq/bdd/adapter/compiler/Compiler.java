package edu.unq.bdd.adapter.compiler;

import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.bytecode.Operation;
import edu.unq.bdd.domain.bytecode.OperationFactory;

import java.util.List;

public class Compiler implements CommandCompiler {
    
    @Override
    public ByteCode parse(String input) {
        return ByteCode.builder()
                .operations(parseOperations(input))
                .build();
    }

    private List<Operation> parseOperations(String input) {
        if(input.startsWith("select")){
            return parseSelect(input);
        } else if (input.startsWith("insert")) {
            return parseInsert(input);
        } else if (input.equals(".exit")) {
            return parseExit(input);
        } else {
            return unrecognizedCommand();
        }
    }

    private List<Operation> unrecognizedCommand() {
        return List.of(OperationFactory.buildInvalidCommand());
    }

    private List<Operation> parseSelect(String input) {
        return List.of(OperationFactory.buildSelect());
    }

    private List<Operation> parseInsert(String input) {
        return List.of(OperationFactory.buildInsert());
    }

    private List<Operation> parseExit(String input) {
        return List.of(OperationFactory.buildExit());
    }
}
