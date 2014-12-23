package ru.fizteh.fivt.students.sautin1.proxy.junit;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.students.sautin1.proxy.storeable.StoreableDatabaseState;
import ru.fizteh.fivt.students.sautin1.proxy.filemap.GeneralTable;
import ru.fizteh.fivt.students.sautin1.proxy.shell.CommandExecuteException;
import ru.fizteh.fivt.students.sautin1.proxy.shell.UserInterruptException;
import ru.fizteh.fivt.students.sautin1.proxy.storeable.AbstractStoreableDatabaseCommand;

/**
 * Size command.
 * Created by sautin1 on 10/12/14.
 */
public class SizeCommand extends AbstractStoreableDatabaseCommand {

    public SizeCommand() {
        super("size", 0, 0);
    }

    /**
     * Get the number of entries in the active table.
     * @param state - database state.
     * @param args - command arguments.
     * @throws UserInterruptException if user desires to exit.
     * @throws CommandExecuteException if any error occurs.
     */
    @Override
    public void execute(StoreableDatabaseState state, String... args)
            throws UserInterruptException, CommandExecuteException {
        if (checkArgumentNumber(args) != CheckArgumentNumber.EQUAL) {
            throw new CommandExecuteException(toString() + ": wrong number of arguments");
        }
        GeneralTable<Storeable> table = state.getActiveTable();
        if (table != null) {
            System.out.println(table.size());
        } else {
            System.err.println("no table");
        }
    }
}
