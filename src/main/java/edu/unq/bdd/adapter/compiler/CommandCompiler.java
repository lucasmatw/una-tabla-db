package edu.unq.bdd.adapter.compiler;

import edu.unq.bdd.domain.bytecode.ByteCode;

public interface CommandCompiler {
    ByteCode parse(String input);
}
