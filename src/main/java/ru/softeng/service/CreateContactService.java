package ru.softeng.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.softeng.form.ContactForm;
import ru.softeng.repository.IContactRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
public class CreateContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactService.class);
    private IContactRepository repos;

    @Autowired
    public CreateContactService(IContactRepository repos) {
        this.repos = repos;
    }

    private boolean isNumeric(String str) {
        boolean regVal = str.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        if(!regVal){
            LOGGER.info("{} is not phone number", str);
        }
        return regVal;
    }

    // Check for image availability
    private boolean imageExist(String url){
        try {
            BufferedImage image = ImageIO.read(new URL(url));
            if(image != null){
                return true;
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Error {}: Malformed URL of image.", e);
        } catch (IOException e) {
            LOGGER.error("Error {}: on a image reading.", e);
        }
        return false;
    }

    public boolean contactValid(BindingResult bindingResult, ContactForm contactForm, UUID id) {
        String foundPhone = repos.getByID(id).getPhone();
        boolean phoneIsUnique;
        if(foundPhone.equals(contactForm.getPhone())){
            phoneIsUnique = true;
        } else {
            phoneIsUnique = (repos.getByPhone(contactForm.getPhone()) == null);
        }

        if(!bindingResult.hasErrors() && isNumeric(contactForm.getPhone()) && phoneIsUnique){
            if(!imageExist(contactForm.getImage())){
                contactForm.setImage(null);
            }
            return true;
        }
        return false;
    }

    public boolean contactValid(BindingResult bindingResult, ContactForm contactForm) {
        boolean phoneIsUnique = (repos.getByPhone(contactForm.getPhone()) == null);
        if(!phoneIsUnique){
            LOGGER.error("Error: Phone {} already exist", contactForm.getPhone());
        }

        if(!bindingResult.hasErrors() && isNumeric(contactForm.getPhone()) && phoneIsUnique){
            if(!imageExist(contactForm.getImage())){
                contactForm.setImage(null);
            }
            return true;
        }
        return false;
    }
}