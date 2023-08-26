package edu.unq.bdd.domain.virtualmachine;

import edu.unq.bdd.domain.bytecode.ByteCode;

public interface VirtualMachine {
    ExecutionResult run(ByteCode byteCode);
}
