package server;

import com.databil.model.Command;
import com.databil.model.Contact;

import com.databil.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ContactService contactService = new ContactService("/home/mirlan/IdeaProjects/AddressBookGUI/ab.json");
        ObjectMapper objectMapper = new ObjectMapper();

        try (ServerSocket serverSocket = new ServerSocket(3333)){
            System.out.println("Server started");
            System.out.println("Waiting for connection");
            System.out.println(serverSocket.getInetAddress());
            System.out.println(serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                //input
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()) );
                //output
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = reader.readLine();
                System.out.println("Accepted request: " + request);
                Command command = objectMapper.readValue(request, Command.class);
                Contact contact = command.contact();

                switch (command.command()) {
                    case LIST_COMMAND -> {
                        List<Contact> contactList = contactService.getContacts();

                        objectMapper.writeValue(writer, contactList);
                    }
                    case NEW_COMMAND -> {
                        contactService.save(contact);
                        System.out.println("Contact saved: " + contact);
                    }
                    case DELETE_COMMAND -> {
                        contactService.delete(contact.getPhone());
                        System.out.println("Contact deleted: " + contact);
                    }
                    case UPDATE_COMMAND -> {
                        contactService.update(contact);
                        System.out.println("Contact updated: " + contact);
                    }
                    case FIND_COMMAND -> {
                       Contact foundContact = contactService.findByPhone(contact.getPhone());
                       System.out.println("Contact found: " + foundContact);

                    }
                    default -> System.out.println("Unknown command");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}