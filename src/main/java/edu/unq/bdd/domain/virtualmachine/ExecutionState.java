package edu.unq.bdd.domain.virtualmachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class ExecutionState {
    private List<String> result;
    private boolean terminate;
    public static ExecutionState empty(){
        return new ExecutionState(List.of(), false);
    }
    public static ExecutionState withResult(String result){
        return new ExecutionState(List.of(result), false);
    }
    public static ExecutionState withResults(List<String> results){
        return new ExecutionState(results, false);
    }
    public static ExecutionState terminate(String result){
        return new ExecutionState(List.of(result), true);
    }
}
