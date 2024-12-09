package com.databil.ui;

import com.databil.service.ContactService;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MainControlPane extends GridPane {
    ContactService contactService;
    ButtonBar buttonBar = new ButtonBar();

    Button newContactButton = new Button("New Contact");
    Button updateButton = new Button("Update Contact");
    Button deleteButton = new Button("Delete");
    Button searchButton = new Button("Search");

    public MainControlPane() {
        contactService = new ContactService("/home/mirlan/IdeaProjects/AddressBookGUI/ab.json");
        buttonBar.getButtons().addAll(newContactButton, updateButton, deleteButton, searchButton);
        this.add(buttonBar, 0,0);
        newContactButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            add(new NewContactForm(contactService), 0,1);
        });
        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
           add(new FindContactPane(contactService), 0, 1);
        });


    }


}
