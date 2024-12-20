package com.databil.util;

import com.databil.model.Contact;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;

public class FileReaderThread implements Runnable {

    private final String FILE_PATH;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<Contact> contacts = new ArrayList<>();

    public FileReaderThread(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    @Override
    public void run() {
        System.out.println("Stared reading file...");
        readContacts();
        System.out.println("Completed reading file, There are " + contacts.size() + " contacts.");
    }

    public void readContacts(){
        try {
            File file = new File(FILE_PATH);
            contacts = objectMapper.readValue(file, new TypeReference<ArrayList<Contact>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Can not read file!");
        }
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }
}
