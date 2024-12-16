package com.databil.ui;

import com.databil.model.Contact;
import com.databil.service.ContactService;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class MainControlPane extends GridPane {
    ContactService contactService;

    HBox hBox = new HBox();
    TableView<Contact> tableView = new TableView();



    public MainControlPane() throws InterruptedException {
        contactService = new ContactService("/home/mirlan/IdeaProjects/AddressBookGUI/ab.json");
        ContactForm contactPane = new ContactForm(new Contact(), contactService);

        TableColumn<Contact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        TableColumn<Contact, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        TableColumn<Contact, String> phoneColumn = new TableColumn<>("phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        tableView.getColumns().addAll(nameColumn, surnameColumn, phoneColumn);
        TableView.TableViewSelectionModel<Contact> selectionModel =
                tableView.getSelectionModel();
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);

        List<Contact> contactList = contactService.getContacts();

        for (Contact contact : contactList) {
            tableView.getItems().add(contact);
        }

        //TO-DO HW1: table refresh

        ObservableList<Contact> selectedItems =
                selectionModel.getSelectedItems();

        selectedItems.addListener(
                new ListChangeListener<Contact>() {
                    @Override
                    public void onChanged(
                            Change<? extends Contact> change) {
                        contactPane.setContact(selectedItems.getFirst());
                    }
                });

        hBox.getChildren().add(tableView);
        hBox.getChildren().add(contactPane);
        add(hBox, 0, 0);
    }


}
