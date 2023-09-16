package edu.unq.bdd;

import edu.unq.bdd.adapter.compiler.CommandCompiler;
import edu.unq.bdd.adapter.compiler.Compiler;
import edu.unq.bdd.domain.bytecode.ByteCode;
import edu.unq.bdd.domain.model.Person;
import edu.unq.bdd.domain.model.PersonStorage;
import edu.unq.bdd.domain.virtualmachine.ExecutionResult;
import edu.unq.bdd.domain.virtualmachine.UnaTablaVM;
import edu.unq.bdd.domain.virtualmachine.VirtualMachine;
import edu.unq.bdd.domain.virtualmachine.persistence.Pager;
import edu.unq.bdd.domain.virtualmachine.persistence.Storage;
import edu.unq.bdd.domain.virtualmachine.persistence.file.BinaryFileConnection;
import edu.unq.bdd.domain.virtualmachine.persistence.file.FileDatabase;

import java.util.LinkedHashMap;
import java.util.Scanner;

import static edu.unq.bdd.domain.model.PersonStorage.RECORD_SIZE;

public class App {

    private static final String DEFAULT_DB_PATH = "database.bin";

    private static final int PAGE_SIZE = 4096;

    public static void main(String[] args) {

        String databasePath = getDatabasePath(args);

        BinaryFileConnection db = new FileDatabase(databasePath);
        Pager pager = new Pager(RECORD_SIZE, PAGE_SIZE, db, new LinkedHashMap<>());

        Storage<Person> personStorage = new PersonStorage(pager);
        CommandCompiler compiler = new Compiler();
        VirtualMachine vm = new UnaTablaVM(personStorage);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("sql>");

            ByteCode byteCode = compiler.parse(scanner.nextLine());
            ExecutionResult result = vm.run(byteCode);

            System.out.println(result.data());
            if (result.terminate()) {
                break;
            }
        }

        scanner.close();
    }

    private static String getDatabasePath(String[] args) {
        if (args.length == 0) {
            return DEFAULT_DB_PATH;
        } else {
            return args[0];
        }
    }
}
