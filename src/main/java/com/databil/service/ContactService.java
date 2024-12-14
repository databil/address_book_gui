package com.databil.service;

import com.databil.model.Contact;
import com.databil.repository.FileRepository;


import java.util.InputMismatchException;
import java.util.List;

public class ContactService {

    private final FileRepository fileRepository;
    private List<Contact> contacts;

    String nameRegex = "^[A-Z][a-zA-Z '.-]*[A-Za-z]$";
    String phoneRegex = "^([+]?\\d{1,3}[-\\s]?|)\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{3}$";

    public ContactService(String filePath) {
        fileRepository = new FileRepository(filePath);
        this.contacts = fileRepository.readContacts();
    }
    //save
    //update
    //delete
    //find

    public void save(Contact contact) throws InputMismatchException {
       validateContact(contact);
       contacts.add(contact);
       fileRepository.writeContacts(contacts);
    }

    public void update(Contact newContact) {
        validateContact(newContact);
        Contact oldContact = findByPhone(newContact.getPhone());
        contacts.remove(oldContact);
        contacts.add(newContact);
        fileRepository.writeContacts(contacts);
    }

    public void delete(String phone) {
        Contact contact = findByPhone(phone);
        contacts.remove(contact);
        fileRepository.writeContacts(contacts);
    }

    public Contact findByPhone(String phone) {
        return contacts.stream().filter(contact -> contact.getPhone().equals(phone)).findFirst().orElse(null);
    }

    public List<Contact> findByPhonePrefix(String phonePrefix) {

        List<Contact> filteredList = contacts.stream().filter(c -> c.getPhone().startsWith(phonePrefix)).toList();

      /*  ArrayList<Contact> foundContacts = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.getPhone().startsWith(phonePrefix)) {
                foundContacts.add(contact);
            }
        }
*/
        return filteredList;
    }

    private void validateContact(Contact contact) throws InputMismatchException {
        if (!contact.getName().matches(nameRegex)) {
            throw new InputMismatchException();
        }
        if (!contact.getSurname().matches(nameRegex)) {
            throw new InputMismatchException();
        }
        if (!contact.getPhone().matches(phoneRegex)) {
            throw new InputMismatchException();
        }
    }

    public String printContacts() {
        return contacts.toString();
    }

    public int getContactSize() {
        return contacts.size();
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
