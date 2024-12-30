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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static int PORT = 3333;
    private final static String FILE_PATH = "/home/mirlan/IdeaProjects/AddressBookGUI/ab.json";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            System.out.println("Waiting for connection");
            System.out.println(serverSocket.getInetAddress());
            System.out.println(serverSocket.getLocalPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClient(Socket clientSocket) throws InterruptedException, IOException {
        ContactService contactService = new ContactService(FILE_PATH);
        //input
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //output
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

        String request = reader.readLine();
        System.out.println("Accepted request: " + request);
        Command command = objectMapper.readValue(request, Command.class);
        Contact contact = command.contact();

        switch (command.command()) {
            case LIST_COMMAND -> {
                List<Contact> contactList = contactService.getContacts(command.userId());
                Response response = new Response(Status.OK, contactList);
                objectMapper.writeValue(writer, response);
            }
            case NEW_COMMAND -> {
                try {
                    contactService.save(contact, command.userId());
                    System.out.println("Contact saved: " + contact);
                    Response response = new Response(Status.OK, List.of(contact));
                    objectMapper.writeValue(writer, response);
                } catch (InputMismatchException ime) {
                    Response response = new Response(Status.ERROR, List.of(contact));
                    objectMapper.writeValue(writer, response);
                }

            }
            case DELETE_COMMAND -> {
                contactService.delete(contact.getPhone(), command.userId());
                System.out.println("Contact deleted: " + contact);
                Response response = new Response(Status.OK, null);
                objectMapper.writeValue(writer, response);
            }
            case UPDATE_COMMAND -> {
                try {
                    contactService.update(contact, command.userId());
                    System.out.println("Contact updated: " + contact);
                    Response response = new Response(Status.OK, List.of(contact));
                    objectMapper.writeValue(writer, response);
                } catch (InputMismatchException ime) {
                    Response response = new Response(Status.ERROR, List.of(contact));
                    objectMapper.writeValue(writer, response);
                }
            }
            case FIND_COMMAND -> {
                Contact foundContact = contactService.findByPhone(contact.getPhone(), command.userId());
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
}

