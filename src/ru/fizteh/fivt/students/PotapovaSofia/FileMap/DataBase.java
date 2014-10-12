package ru.fizteh.fivt.students.PoatpovaSofia.FileMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DataBase {
    private static String dbPath;
    private static Map<String, String> db;

    public DataBase(String path) throws Exception {
        dbPath = path;
        File file = new File(path).getAbsoluteFile();
        if (file.exists()) {
            db = readDataFromFile();
        } else {
            file = new File(dbPath);
            file.createNewFile();
            db = new HashMap<>();
        }
    }

    private String readWord(DataInputStream dataInputStream) throws Exception {
        int length = dataInputStream.readInt();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(dataInputStream.readChar());
        }
        return stringBuilder.toString();
    }

    public Map<String, String> readDataFromFile() throws Exception {
        Map<String, String> resultFileMap = new HashMap<>();
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dbPath));
        while (true) {
            try {
                String key = readWord(dataInputStream);
                String value = readWord(dataInputStream);
                resultFileMap.put(key, value);
            } catch (Exception e) {
                break;
            }
        }
        return resultFileMap;
    }

    public final Map<String, String> getDataBase() {
        return db;
    }

    private void writeWord(DataOutputStream dataOutputStream, String str) throws Exception {
        dataOutputStream.writeInt(str.length());
        dataOutputStream.writeChars(str);
    }

    public void writeDataToFile() throws Exception {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(dbPath));
        Set<String> keySet = db.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            writeWord(dataOutputStream, key);
            writeWord(dataOutputStream, db.get(key));
        }
    }
}
