package ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.MultiFileHashMapCommands;

import java.io.IOException;

import ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.MultiFileHashMapManager;

public final class UseCommand extends MultiFileHashMapCommand {
    @Override
    public void execute(final String[] args, MultiFileHashMapManager parser)
            throws IOException {
        checkArgs(2, args);
        if (parser.getTables().get(args[1]) != null) {
            parser.setWorkTable(args[1]);
            System.out.println("using " + args[1]);
        } else {
            System.out.println(args[1] + " not exists");
        }
    }
}
