package edu.unq.bdd.domain.bytecode;


import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class Operation {
    private OpCode opCode;
    private List<String> fields;
}
