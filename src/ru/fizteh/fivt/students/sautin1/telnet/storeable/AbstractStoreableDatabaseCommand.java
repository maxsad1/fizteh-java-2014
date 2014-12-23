package ru.fizteh.fivt.students.sautin1.telnet.storeable;

import ru.fizteh.fivt.students.sautin1.telnet.shell.AbstractCommand;

/**
 *
 * Created by sautin1 on 10/12/14.
 */
public abstract class AbstractStoreableDatabaseCommand extends AbstractCommand<StoreableDatabaseState> {

    public AbstractStoreableDatabaseCommand(String name, int minArgNumber, int maxArgNumber) {
        super(name, minArgNumber, maxArgNumber);
    }
}
