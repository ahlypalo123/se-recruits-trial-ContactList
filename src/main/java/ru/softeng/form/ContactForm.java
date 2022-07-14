package ru.softeng.form;

import javax.validation.constraints.Size;

public class ContactForm {
    @Size(max=100)
    private String image;

    @Size(min=2, max=30)
    private String name;

    @Size(max=40)
    private String description;

    @Size(min=6, max=12)
    private String phone;

    public ContactForm(){}

    public ContactForm(String name, String image, String description, String phone) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.phone = phone;
    }

    public String getName()   {
        return this.name;
    }

    public String getDescription()  {
        return this.description;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}