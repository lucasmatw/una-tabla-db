package edu.unq.bdd.domain.virtualmachine;

import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.bytecode.Operation;

public class UnaTablaVM implements VirtualMachine {

    @Override
    public ExecutionResult run(ByteCode byteCode) {
        ExecutionState state = ExecutionState.empty();

        for (Operation op : byteCode.getOperations()) {
            state = executeOperation(op, state);
        }
        return buildResult(state);
    }

    private ExecutionResult buildResult(ExecutionState state) {
        return new ExecutionResult(state.getResult(), state.isTerminate());
    }


    private ExecutionState executeOperation(Operation operation, ExecutionState state){
        switch (operation.getOpCode()) {
            case EXIT:
                return executeExit();
            default:
                return executeNotImplemented(operation);
        }
    }

    private ExecutionState executeExit() {
        return new ExecutionState("Terminado", true);
    }

    private ExecutionState executeNotImplemented(Operation operation) {
        return new ExecutionState(String.format("%s no implementado", operation.getOpCode().name()), false);
    }
}
