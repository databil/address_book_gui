package com.databil.service;

import com.databil.model.Contact;
import com.databil.repository.FileRepository;


import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ContactService {

    private final FileRepository fileRepository;
    private final List<Contact> contacts;

    String nameRegex = "^[A-Z][a-zA-Z '.-]*[A-Za-z]$";
    String phoneRegex = "^([+]?\\d{1,3}[-\\s]?|)\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{3}$";

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ContactService(String filePath) throws InterruptedException {
        fileRepository = new FileRepository(filePath);
        this.contacts = fileRepository.readContacts();
    }
    //save
    //update
    //delete
    //find

    public void save(Contact contact, String userId) throws InputMismatchException {
       validateContact(contact);
       lock.writeLock().lock();
       try {
           contacts.add(contact);
           fileRepository.writeContacts(contacts);
       } finally {
           lock.writeLock().unlock();
       }

    }

    public void update(Contact newContact, String userId) throws InputMismatchException {
        validateContact(newContact);
        lock.writeLock().lock();
        try {
            Contact oldContact = findByPhone(newContact.getPhone(), userId);
            contacts.remove(oldContact);
            contacts.add(newContact);
        } finally {
            lock.writeLock().unlock();
        }

    }

    public void delete(String phone, String userId) throws InputMismatchException {
        lock.writeLock().lock();
        try {
            Contact contact = findByPhone(phone, userId);
            contacts.remove(contact);
        } finally {
            lock.writeLock().unlock();
        }

    }

    public Contact findByPhone(String phone, String userId) {
        lock.readLock().lock();
        try {
            return contacts.stream().filter(contact -> contact.getPhone().equals(phone) && contact.getUserId().equals(userId)).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }

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

    public List<Contact> getContacts(String userId) {
        return contacts.stream().filter(contact -> contact.getUserId().equals(userId)).toList();
    }
}
