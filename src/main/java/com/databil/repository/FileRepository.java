package com.databil.repository;

import com.databil.model.Contact;
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
        ArrayList<Contact> contacts = new ArrayList<>();
       try {
           File file = new File(FILE_PATH);
           contacts = objectMapper.readValue(file, new TypeReference<ArrayList<Contact>>() {});
           return contacts;
       } catch (Exception e) {
           return contacts;
       }
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
