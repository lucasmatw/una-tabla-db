package edu.unq.bdd.domain.bytecode;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ByteCode {
    private List<Operation> operations;
}
