package edu.unq.bdd;

import edu.unq.bdd.adapter.compiler.CommandCompiler;
import edu.unq.bdd.adapter.compiler.Compiler;
import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.model.Person;
import edu.unq.bdd.domain.model.PersonStorage;
import edu.unq.bdd.domain.virtualmachine.ExecutionResult;
import edu.unq.bdd.domain.virtualmachine.UnaTablaVM;
import edu.unq.bdd.domain.virtualmachine.VirtualMachine;
import edu.unq.bdd.domain.virtualmachine.persistence.Storage;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {

    private static final int PAGE_SIZE = 4096;
    public static void main(String[] args) {

        Storage<Person> personStorage = new PersonStorage(PAGE_SIZE);
        CommandCompiler compiler = new Compiler();
        VirtualMachine vm = new UnaTablaVM(personStorage);

        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("sql>");
            command = scanner.nextLine();

            ByteCode byteCode = compiler.parse(command);
            ExecutionResult result = vm.run(byteCode);

            System.out.println(result.data());
            if (result.terminate()) {
                break;
            }
        }

        scanner.close();
    }
}
