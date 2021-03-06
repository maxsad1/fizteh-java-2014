package ru.fizteh.fivt.students.ryad0m.parallel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TableNode {

    private Path file;
    private HashMap<String, String> hashMap;

    public TableNode(Path nodeFile) throws IOException {
        file = nodeFile;
        hashMap = new HashMap<>();
        if (file.toFile().exists()) {
            byte[] bytes = Files.readAllBytes(file);
            int off = 0;
            while (off < bytes.length) {
                int stringLen = byteToInt(bytes, off, 4);
                off += 4;
                String key = byteToString(bytes, off, stringLen);
                off += stringLen;
                stringLen = byteToInt(bytes, off, 4);
                off += 4;
                String value = byteToString(bytes, off, stringLen);
                off += stringLen;
                put(key, value);
            }
        }
    }

    private int byteToInt(byte[] bytes, int off, int len) throws IOException {
        if (off + len > bytes.length) {
            throw new IOException("File corrupted.");
        }
        int res = 0;
        for (int i = off; i - off < len; ++i) {
            res <<= 8;
            res += (int) bytes[i];
        }
        return res;
    }

    private byte[] intToByte(int x) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; ++i) {
            bytes[3 - i] = (byte) (x % 256);
            x >>= 8;
        }
        return bytes;
    }

    private String byteToString(byte[] bytes, int off, int len)
            throws IOException {
        if (off + len > bytes.length) {
            throw new IOException("File corrupted.");
        }
        return new String(bytes, off, len, "UTF-8");
    }

    private void writeString(BufferedOutputStream bufferedOutputStream, String string) throws IOException {
        byte[] bytes = string.getBytes("UTF-8");
        bufferedOutputStream.write(intToByte(bytes.length));
        bufferedOutputStream.write(bytes);
    }


    public void save() throws IOException {
        file.toFile().delete();
        if (!hashMap.isEmpty()) {
            if (!file.getParent().toFile().exists()) {
                file.getParent().toFile().mkdirs();
            }
            file.toFile().createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file.toFile());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                writeString(bufferedOutputStream, entry.getKey());
                writeString(bufferedOutputStream, entry.getValue());
            }
            bufferedOutputStream.close();
            fileOutputStream.close();
        }
    }

    public void put(String key, String value) {
        if (value == null) {
            remove(key);
        } else {
            hashMap.put(key, value);
        }
    }

    public boolean containKey(String key) {
        return hashMap.containsKey(key);
    }

    public String get(String key) {
        return hashMap.get(key);
    }

    public void remove(String key) {
        if (hashMap.containsKey(key)) {
            hashMap.remove(key);
        }
    }

    public Set<String> getKeys() {
        return hashMap.keySet();
    }

    public int getSize() {
        return hashMap.size();
    }
}
