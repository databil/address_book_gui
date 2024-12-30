package com.databil.repository;

import com.databil.model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {

    private final String FILE_PATH;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Object fileLock = new Object(); // Lock for file operations

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
        synchronized (fileLock) {
            try (FileReader reader = new FileReader(FILE_PATH)) {
               return objectMapper.readValue(reader, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Contact.class));
            } catch (IOException ex){
                System.out.println(ex.getMessage());
                return new ArrayList<>();
            }
        }
    }

    /*
    open file
    List of Contacts convert to String: name; surname; phone
    write to the file
     */
    public void writeContacts(List<Contact> contacts) {
        synchronized (fileLock) {
            try {
                objectMapper.writeValue(new File(FILE_PATH), contacts);
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
