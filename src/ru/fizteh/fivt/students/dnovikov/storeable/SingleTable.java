package ru.fizteh.fivt.students.dnovikov.storeable;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.students.dnovikov.storeable.Exceptions.LoadOrSaveException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

public class SingleTable {
    public static final int FOLDERS_COUNT = 16;
    public static final int FILES_COUNT = 16;
    private static final String CODING = "UTF-8";
    private Map<String, Storeable> dataBase;
    private Path singleTablePath;
    private DataBaseTable parentTable;
    private int folderNumber;
    private int fileNumber;

    public SingleTable(int folderNum, int fileNum, DataBaseTable parent) throws LoadOrSaveException {
        dataBase = new TreeMap<>();
        parentTable = parent;
        folderNumber = folderNum;
        fileNumber = fileNum;
        singleTablePath = getPathToSaveOrLoad();
        this.load();
    }

    public Storeable put(String key, Storeable value) {
        return dataBase.put(key, value);
    }

    public Storeable get(String key) {
        return dataBase.get(key);
    }

    public List<String> list() {
        Set<String> keys = dataBase.keySet();
        List<String> result = new ArrayList<>();
        result.addAll(keys);
        return result;
    }

    public Storeable remove(String key) {
        return dataBase.remove(key);
    }

    public void save() throws LoadOrSaveException {
        if (singleTablePath.toFile().isDirectory()) {
            throw new LoadOrSaveException("cannot load table: '" + singleTablePath.getFileName() + "' is directory");
        } else {
            File pathToSave = getPathToSaveOrLoad().toFile();
            if (dataBase.size() == 0) {
                if (pathToSave.exists()) {
                    if (!pathToSave.delete()) {
                        throw new LoadOrSaveException("can't delete " + pathToSave.getAbsolutePath());
                    }
                }
                File parentDir = new File(pathToSave.getParent());
                String[] list = parentDir.list();
                if (parentDir.exists() && (list == null || list.length == 0)) {
                    if (!parentDir.delete()) {
                        throw new LoadOrSaveException("can't delete directory " + parentDir);
                    }
                }
            } else {
                if (!getFolder().toFile().exists()) {
                    try {
                        Files.createDirectory(getFolder());
                    } catch (IOException e) {
                        throw new LoadOrSaveException("can't create directory: " + getFolder().toAbsolutePath());
                    }
                }
                try (DataOutputStream outputStream = new DataOutputStream(Files.newOutputStream(singleTablePath))) {
                    Set<Entry<String, Storeable>> list = dataBase.entrySet();
                    for (Entry<String, Storeable> entry : list) {
                        Storeable value = entry.getValue();
                        String serializedValue = parentTable.getDatabaseConnector().serialize(parentTable, value);
                        writeString(outputStream, entry.getKey());
                        writeString(outputStream, serializedValue);
                    }
                } catch (IOException e) {
                    throw new LoadOrSaveException("cannot write to database");
                }
            }
        }
    }

    public Path getFolder() {
        return parentTable.getTableDirectory().resolve(folderNumber + ".dir");
    }

    private Path getPathToSaveOrLoad() {
        return getFolder().resolve(fileNumber + ".dat");
    }

    private boolean checkKey(String key) {
        int hashCode = key.hashCode();
        int directoryNumber = hashCode % FOLDERS_COUNT;
        int fileNumber = hashCode / FOLDERS_COUNT % FILES_COUNT;
        if (directoryNumber < 0) {
            directoryNumber += FOLDERS_COUNT;
        }
        if (fileNumber < 0) {
            fileNumber += FILES_COUNT;
        }
        return (directoryNumber == this.folderNumber && fileNumber == this.fileNumber);
    }

    private void load() throws LoadOrSaveException {
        if (getFolder().toFile().isFile()) {
            throw new LoadOrSaveException("cannot load table: '" + getFolder().toAbsolutePath() + "' is not directory");
        }
        if (singleTablePath.toFile().isDirectory()) {
            throw new LoadOrSaveException("cannot load table: '" + singleTablePath.toFile().toString()
                    + "' is directory");
        } else if (singleTablePath.toFile().exists()) {
            try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(singleTablePath))) {
                dataBase.clear();
                while (inputStream.available() > 0) {
                    String key = readString(inputStream);
                    String value = readString(inputStream);
                    if (!checkKey(key)) {
                        throw new LoadOrSaveException("cannot load from database: format error");
                    }
                    StoreableType deserializedValue =
                            (StoreableType) parentTable.getDatabaseConnector().deserialize(parentTable, value);
                    dataBase.put(key, deserializedValue);
                }
            } catch (IOException e) {
                try (DataOutputStream outputStream = new DataOutputStream(Files.newOutputStream(singleTablePath))) {
                    outputStream.flush();
                } catch (IOException | SecurityException exception) {
                    throw new LoadOrSaveException("cannot create file with database");
                }
            } catch (OutOfMemoryError e) {
                throw new LoadOrSaveException("cannot read from database");
            } catch (ParseException e) {
                throw new LoadOrSaveException("cannot load from database: format error");
            }
        }
    }

    private String readString(DataInputStream inputStream) throws IOException {
        int length = inputStream.readInt();
        if (length <= 0) {
            throw new IOException("cannot read from database");
        }
        byte[] byteString = new byte[length];
        inputStream.readFully(byteString);
        return new String(byteString, CODING);

    }

    private void writeString(DataOutputStream outputStream, String string) throws IOException {
        try {
            byte[] stringByte = string.getBytes(CODING);
            outputStream.writeInt(stringByte.length);
            outputStream.write(stringByte);
        } catch (IOException e) {
            throw new IOException("cannot write to database");
        }
    }

    public void drop() throws LoadOrSaveException {
        dataBase.clear();
        save();
    }

    public int size() {
        return dataBase.size();
    }
}
