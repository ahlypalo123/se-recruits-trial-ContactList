package ru.softeng.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.softeng.repository.IContactRepository;
import ru.softeng.form.ContactForm;
import ru.softeng.model.Contact;
import ru.softeng.service.CreateContactService;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    private CreateContactService createContactService;
    private IContactRepository repos;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    public ContactController(IContactRepository repos, CreateContactService createContactService) {
        this.repos = repos;
        this.createContactService = createContactService;
    }

    //Empty view
    @GetMapping("/create")
    public ModelAndView getCreateForm(){
        ModelAndView mav = new ModelAndView("create");
        return mav;
    }

    //Create contact:
    @PostMapping("/save")
    @ResponseBody
    public boolean putNewContact(@Valid @RequestBody ContactForm contactForm, BindingResult bindingResult) {
        boolean createContactResult = createContactService.contactValid(bindingResult, contactForm);

        if (createContactResult) {
            repos.addContact(contactForm);
        } else {
            LOGGER.info("incorrect fields on contact saving");
        }

        return createContactResult;
    }

    //Edit contact:
    @GetMapping("/{id}/edit")
    public ModelAndView getEditForm(@PathVariable UUID id) {
        Contact contact = repos.getByID(id);

        if(contact == null){
            LOGGER.error("Cannot edit contact: ID = {}", id);
            return new ModelAndView("redirect:/contacts");
        }
        ModelAndView mav = new ModelAndView("create");
        mav.addObject("id", id);
        return mav;
    }

    //Push processing 'Submit':
    @PutMapping("/{id}/edit")
    @ResponseBody
    public boolean putEditedContact(@PathVariable UUID id, @Valid @RequestBody ContactForm contactForm, BindingResult bindingResult) {
        boolean createContactResult = createContactService.contactValid(bindingResult, contactForm, id);

        if (createContactResult) {
            repos.editContact(id, contactForm);
        } else {
            LOGGER.info("incorrect fields on contact editing");
        }

        return createContactResult;
    }

    //Delete contact:
    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable("id") UUID id) {
        repos.delete(id);
    }

    //Show list of contacts:
    @GetMapping
    public ModelAndView getList(@RequestParam(required = false) String name) {
        List<Contact> contacts;
        ModelAndView mav = new ModelAndView("list");

        if(StringUtils.hasText(name)) {
            contacts = repos.getByName(name);
        } else {
            contacts = repos.getAll();
        }

        mav.addObject("contacts", contacts);
        return mav;
    }

    @GetMapping("/{id}/get")
    @ResponseBody
    public Contact getContact(@PathVariable UUID id) {
        return repos.getByID(id);
    }
}