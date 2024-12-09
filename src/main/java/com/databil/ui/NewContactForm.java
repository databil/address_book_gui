package com.databil.ui;

import com.databil.model.Contact;
import com.databil.service.ContactService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class NewContactForm extends GridPane {

    private ContactService contactService;

    public NewContactForm(ContactService contactService1) {

        contactService = contactService1;
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        Label nameLabel = new Label("Name");
        Label surnameLabel = new Label("Surname");
        Label phoneLabel = new Label("Phone");

        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField phoneField = new TextField();

        add(nameLabel, 0, 0);
        add(nameField, 1, 0);

        add(surnameLabel, 0, 1);
        add(surnameField, 1, 1);

        add(phoneLabel, 0, 2);
        add(phoneField, 1, 2);

        add(saveButton, 0, 3);
        add(cancelButton, 1, 3);

        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Contact contact = new Contact();
            contact.setName(nameField.getText());
            contact.setSurname(surnameField.getText());
            contact.setPhone(phoneField.getText());
            contactService.save(contact);

            nameField.clear();
            surnameField.clear();

            phoneField.clear();
        });
    }
}
