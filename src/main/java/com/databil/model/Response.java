package com.databil.model;

import java.util.List;

public record Response(Status status, List<Contact> contactList) {
}
