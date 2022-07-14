package ru.softeng.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.softeng.form.ContactForm;
import ru.softeng.model.Contact;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class ContactRepositoryImpl implements IContactRepository {

    private JdbcTemplate template;

    private static final String SELECT_ALL_SQL = "SELECT * FROM contacts";
    private static final String SELECT_CONTACTS_BY_NAME_SQL = "SELECT * FROM contacts WHERE name=?";
    private static final String ADD_CONTACT_SQL = "INSERT INTO contacts (name, image, description, phone) VALUES (?, ?, ?, ?)";
    private static final String DELETE_CONTACT_BY_ID_SQL = "DELETE FROM contacts WHERE id=?";
    private static final String SELECT_CONTACT_BY_ID_SQL = "SELECT * FROM contacts WHERE id=?";
    private static final String EDIT_CONTACT_BY_ID_SQL = "UPDATE contacts SET name=?, image=?, description=?, phone=? WHERE id=?";
    private static final String SELECT_CONTACT_BY_PHONE_SQL = "SELECT * FROM contacts WHERE phone=?";

    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String DESCRIPTION = "description";
    private static final String PHONE = "phone";


    private RowMapper<Contact> rowMapper = (ResultSet resultSet, int i) -> new Contact(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString(NAME),
                resultSet.getString(IMAGE),
                resultSet.getString(DESCRIPTION),
                resultSet.getString(PHONE));

    @Autowired
    public ContactRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void addContact(ContactForm contactForm) {
        template.update(ADD_CONTACT_SQL, contactForm.getName(), contactForm.getImage(), contactForm.getDescription(), contactForm.getPhone());
    }

    @Transactional
    public void editContact(UUID id, ContactForm contactForm) {
        template.update(EDIT_CONTACT_BY_ID_SQL, contactForm.getName(), contactForm.getImage(), contactForm.getDescription(), contactForm.getPhone(), id);
    }

    @Transactional
    public void delete(UUID id) {
        template.update(DELETE_CONTACT_BY_ID_SQL, id);
    }

    public List<Contact> getByName(String name) {
        return template.query(SELECT_CONTACTS_BY_NAME_SQL, rowMapper, name);
    }

    public Contact getByID(UUID id) {
        try {
            return template.queryForObject(SELECT_CONTACT_BY_ID_SQL, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Contact> getAll() {
        return template.query(SELECT_ALL_SQL, rowMapper);
    }

    public Contact getByPhone(String phone) {
        try {
            return template.queryForObject(SELECT_CONTACT_BY_PHONE_SQL, rowMapper, phone);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}