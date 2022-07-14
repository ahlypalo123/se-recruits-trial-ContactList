package ru.softeng.model;

import java.util.UUID;

public class Contact {
    private UUID id;
    private String image;
    private String name;
    private String description;
    private String phone;

    public Contact() {

    }

    public Contact(UUID id, String name, String image, String description, String phone) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}