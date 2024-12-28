package server;

import com.databil.model.Command;
import com.databil.model.Contact;

import com.databil.model.Response;
import com.databil.model.Status;
import com.databil.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
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
                        Response response = new Response(Status.OK, contactList);
                        objectMapper.writeValue(writer, response);
                    }
                    case NEW_COMMAND -> {
                        try {
                            contactService.save(contact);
                            System.out.println("Contact saved: " + contact);
                            Response response = new Response(Status.OK, List.of(contact));
                            objectMapper.writeValue(writer, response);
                        } catch (InputMismatchException ime) {
                            Response response = new Response(Status.ERROR, List.of(contact));
                            objectMapper.writeValue(writer, response);
                        }

                    }
                    case DELETE_COMMAND -> {
                        contactService.delete(contact.getPhone());
                        System.out.println("Contact deleted: " + contact);
                        Response response = new Response(Status.OK, null);
                        objectMapper.writeValue(writer, response);
                    }
                    case UPDATE_COMMAND -> {
                        try {
                            contactService.update(contact);
                            System.out.println("Contact updated: " + contact);
                            Response response = new Response(Status.OK, List.of(contact));
                            objectMapper.writeValue(writer, response);
                        } catch (InputMismatchException ime) {
                            Response response = new Response(Status.ERROR, List.of(contact));
                            objectMapper.writeValue(writer, response);
                        }
                    }
                    case FIND_COMMAND -> {
                       Contact foundContact = contactService.findByPhone(contact.getPhone());
                       System.out.println("Contact found: " + foundContact);
                       Response response;
                       if (foundContact != null) {
                           response = new Response(Status.OK, List.of(foundContact));
                       } else {
                           response = new Response(Status.OK, null);
                       }
                       objectMapper.writeValue(writer, response);
                    }
                    default -> System.out.println("Unknown command");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
