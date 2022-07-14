package ru.softeng.repository;

import ru.softeng.form.ContactForm;
import ru.softeng.model.Contact;
import java.util.List;
import java.util.UUID;

public interface IContactRepository {
    void addContact(ContactForm contactForm);
    void editContact(UUID id, ContactForm contactForm);
    void delete(UUID id);
    List<Contact> getByName(String name);
    List<Contact> getAll();
    Contact getByID(UUID id);
    Contact getByPhone(String phone);
}
