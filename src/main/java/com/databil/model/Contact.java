package com.databil.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {

    private String name;

    private String surname;

    private String phone;

    public Contact() {}
    public Contact(@JsonProperty("name") String name,
                   @JsonProperty("surname") String surname,
                   @JsonProperty("phone") String phone) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
