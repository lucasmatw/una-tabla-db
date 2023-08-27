package edu.unq.bdd.domain.virtualmachine;

import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.bytecode.Operation;

public class UnaTablaVM implements VirtualMachine {

    @Override
    public ExecutionResult run(ByteCode byteCode) {
        ExecutionState finalState = byteCode.getOperations().stream()
                .reduce(ExecutionState.empty(), (state, op) -> executeOperation(op, state),
                        (s1, s2) -> s2);
        return buildResult(finalState);
    }

    private ExecutionResult buildResult(ExecutionState state) {
        return new ExecutionResult(state.getResult(), state.isTerminate());
    }

    private ExecutionState executeOperation(Operation operation, ExecutionState state){
        switch (operation.getOpCode()) {
            case EXIT:
                return executeExit();
            case SELECT:
            case INSERT:
                return executeNotImplemented(operation);
            default:
                return executeInvalidCommand();
        }
    }

    private ExecutionState executeInvalidCommand() {
        return new ExecutionState("Comando inválido", false);
    }

    private ExecutionState executeExit() {
        return new ExecutionState("Terminado", true);
    }

    private ExecutionState executeNotImplemented(Operation operation) {
        return new ExecutionState(String.format("%s no implementado", operation.getOpCode().name()), false);
    }
}
