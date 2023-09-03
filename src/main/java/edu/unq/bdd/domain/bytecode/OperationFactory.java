package edu.unq.bdd.domain.bytecode;

import java.util.List;

public class OperationFactory {
    public static Operation buildSelect() {
        return withOpCode(OpCode.SELECT)
                .build();
    }

    public static Operation buildInsert(List<String> fields) {
        return withOpCode(OpCode.INSERT)
                .fields(fields)
                .build();

    }

    public static Operation buildExit() {
        return withOpCode(OpCode.EXIT)
                .build();
    }

    public static Operation buildInvalidCommand() {
        return withOpCode(OpCode.INVALID)
                .build();
    }

    public static Operation buildMetadataCommand() {
        return withOpCode(OpCode.METADATA)
                .build();
    }

    private static Operation.OperationBuilder withOpCode(OpCode opCode) {
        return Operation.builder()
                .opCode(opCode);
    }
}
