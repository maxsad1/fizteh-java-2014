package ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.MultiFileHashMapCommands;

import java.io.IOException;

import ru.fizteh.fivt.students.valentine_lebedeva.Table;
import ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.MultiFileHashMapManager;
import ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.filemap.Cmd.Command;

public class MultiFileHashMapCommand extends Command {
    @Override
    public void execute(final String[] args, MultiFileHashMapManager parser)
            throws IOException {
        // TODO Auto-generated method stub
    }

    public final boolean checkWorkTable(final Table workTable) {
        if (workTable == null) {
            System.out.println("no table");
            return false;
        } else {
            return true;
        }
    }

}
