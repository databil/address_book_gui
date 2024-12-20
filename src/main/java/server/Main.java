package server;

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

                if (request.equals("command:get:list")) {
                    List<Contact> contactList = contactService.getContacts();
                    objectMapper.writeValue(writer, contactList);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
