package ru.fizteh.fivt.students.andreyzakharov.parallelfilemap.commands;

import ru.fizteh.fivt.students.andreyzakharov.parallelfilemap.CommandInterruptException;
import ru.fizteh.fivt.students.andreyzakharov.parallelfilemap.MultiFileTableProvider;

public class RollbackCommand implements Command {
    @Override
    public String execute(MultiFileTableProvider connector, String... args) throws CommandInterruptException {
        if (args.length > 1) {
            throw new CommandInterruptException("rollback: too many arguments");
        }
        if (connector.getCurrent() == null) {
            throw new CommandInterruptException("no table");
        }
        return Integer.toString(connector.getCurrent().rollback());
    }

    @Override
    public String toString() {
        return "rollback";
    }
}
