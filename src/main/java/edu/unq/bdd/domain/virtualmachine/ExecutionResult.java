package edu.unq.bdd.domain.virtualmachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public record ExecutionResult(String data, boolean terminate) {
}
