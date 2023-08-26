package edu.unq.bdd.domain.virtualmachine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExecutionResult {
    private final String data;
    private final boolean terminate;
}
