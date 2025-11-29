package com.library.backend;

import com.library.model.LibraryData;

import java.io.*;

public class DataStore {

    private static final String DATA_FILE = "library-data.bin";

    public static LibraryData load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new LibraryData();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (LibraryData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // In case of error, start with fresh data
            return new LibraryData();
        }
    }

    public static void save(LibraryData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
