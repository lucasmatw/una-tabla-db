package edu.unq.bdd.adapter.compiler;

import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.bytecode.OperationFactory;

import java.util.List;

public class Compiler implements CommandCompiler {
    
    @Override
    public ByteCode parse(String input) {
        if(input.startsWith("select")){
            return parseSelect(input);
        } else if (input.startsWith("insert")) {
            return parseInsert(input);
        } else if (input.equals(".exit")) {
            return parseExit(input);
        }

        return ByteCode.builder()
                .operations(List.of())
                .build();
    }

    private ByteCode parseSelect(String input) {
        return ByteCode.builder()
                .operations(List.of(OperationFactory.buildSelect()))
                .build();
    }

    private ByteCode parseInsert(String input) {
        return ByteCode.builder()
                .operations(List.of(OperationFactory.buildInsert()))
                .build();
    }

    private ByteCode parseExit(String input) {
        return ByteCode.builder()
                .operations(List.of(OperationFactory.buildExit()))
                .build();
    }
}
