package ru.fizteh.fivt.students.AliakseiSemchankau.parallel2;

import java.util.Vector;

/**
 * Created by Aliaksei Semchankau on 17.10.2014.
 */
public interface TableInterface {

    void makeCommand(Vector<String> args, DatabaseProvider dProvider);

}