package edu.unq.bdd.domain.bytecode;

public class OperationFactory {
    public static Operation buildSelect(){
        return new Operation(OpCode.SELECT);
    }
    public static Operation buildInsert(){
        return new Operation(OpCode.INSERT);
    }
    public static Operation buildExit(){
        return new Operation(OpCode.EXIT);
    }
}
