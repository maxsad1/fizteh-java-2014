package ru.fizteh.fivt.students.hromov_igor.multifilemap.commands;

public class Rollback extends ParentCommand {


    public Rollback(CommandState state) {
        super(state);
    }

    @Override
    public void run() {
        if (state.getUsingTable() == null) {
            System.out.println("no table");
        } else {
            System.out.println(state.getUsingTable().rollback());
        }
    }

    @Override
    public int requiredArgsNum() {
        return 0;
    }
}
