package ru.fizteh.fivt.students.sautin1.shell;

import java.io.IOException;

/**
 * Created by sautin1 on 9/30/14.
 */
public class CommandMkDir implements Command {
    @Override
    public void execute(String... args) throws RuntimeException, IOException {

    }

    @Override
    public String toString() {
        return "mkdir";
    }
}
