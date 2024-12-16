package com.databil.repository;

import com.databil.model.Contact;
import com.databil.util.FileReaderThread;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {

    private final String FILE_PATH;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileRepository(String filePath) {
        this.FILE_PATH = filePath;
    }

    /*
      open file
      read records from file : name; surname; phone
      parse to Contact object
      add to List
      return list of contacts
     */
    public ArrayList<Contact> readContacts() {
        FileReaderThread fileReaderThread = new FileReaderThread(FILE_PATH);
        Thread thread = new Thread(fileReaderThread);

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            thread.interrupt();
            throw new RuntimeException(e);
        }
        return fileReaderThread.getContacts();
    }

    /*
    open file
    List of Contacts convert to String: name; surname; phone
    write to the file
     */
    public void writeContacts(List<Contact> contacts) {
        try {
           objectMapper.writeValue(new File(FILE_PATH), contacts);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
