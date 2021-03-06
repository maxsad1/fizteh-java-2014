package ru.fizteh.fivt.students.andrey_reshetnikov.JUnit;


import ru.fizteh.fivt.students.andrey_reshetnikov.MultiFileHashMap.Command;
import ru.fizteh.fivt.students.andrey_reshetnikov.MultiFileHashMap.Table;
import ru.fizteh.fivt.students.andrey_reshetnikov.MultiFileHashMap.ConstClass;
import java.util.*;

public class HybridTable {
    private Table cleanTable;
    private FancyTable dirtyTable;
    List<Command> changes;

    public Table getCleanTable() {
        return cleanTable;
    }

    public FancyTable getDirtyTable() {
        return dirtyTable;
    }

    public HybridTable(Table ordinaryTable) throws Exception {
        cleanTable = ordinaryTable;
        dirtyTable = new FancyTable();
        changes = new ArrayList<>();
        rollBack();
    }

    public int rollBack() {
        int ans = diffTables(cleanTable, dirtyTable);
        dirtyTable.importMap(cleanTable);
        changes.clear();
        return ans;
    }

    public int uncommitedChanges() {
        return diffTables(cleanTable, dirtyTable);
    }

    public int commit() throws Exception {
        int ans = diffTables(cleanTable, dirtyTable);
        for (Command command: changes) {
            command.executeOnTable(cleanTable);
        }
        changes.clear();
        return ans;
    }

    private static int diffTables(Table first, Table second) {
        int result = 0;
        for (int i = 0; i < ConstClass.NUM_DIRECTORIES; i++) {
            for (int j = 0; j < ConstClass.NUM_FILES; j++) {
                if (first.databases[i][j] == null && second.databases[i][j] != null) {
                    result += second.databases[i][j].data.size();
                } else if (first.databases[i][j] != null && second.databases[i][j] == null) {
                    result += first.databases[i][j].data.size();
                } else if (first.databases[i][j] != null && second.databases[i][j] != null) {
                    result += diffHashMaps(first.databases[i][j].data, second.databases[i][j].data);
                }
            }
        }
        return result;
    }

    private static int diffHashMaps(HashMap<String, String> first, HashMap<String, String> second) {
        int result = 0;
        for (Map.Entry<String, String> entry: first.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (second.containsKey(key) && !second.get(key).equals(value)) {
                result++;
            }
        }
        HashSet<String> intersect = new HashSet<>(first.keySet());
        intersect.retainAll(second.keySet());
        result += first.size() + second.size() - 2 * intersect.size();
        return result;
    }
}
