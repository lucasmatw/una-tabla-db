package edu.unq.bdd.domain.virtualmachine;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutionState {
    private String result;
    private boolean terminate;
    public static ExecutionState empty(){
        return new ExecutionState("", false);
    }
}
