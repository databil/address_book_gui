package com.databil.ui;

import com.databil.model.Contact;
import com.databil.service.ContactService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FindContactPane extends javafx.scene.layout.GridPane {

    private final ContactService contactService;

    public FindContactPane(ContactService contactService1) {
        this.contactService = contactService1;
        Label phoneLabel = new Label("Phone Number");

        TextField phoneField = new TextField();
        Button findButton = new Button("Find");


        add(phoneLabel, 0, 0);
        add(phoneField, 1, 0);
        add(findButton, 2, 0);

        TextArea contactArea = new TextArea();
        add(contactArea, 0, 2);

        findButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Contact contact = contactService.findByPhone(phoneField.getText());
            contactArea.setText(contact.toString());
        });


    }
}
