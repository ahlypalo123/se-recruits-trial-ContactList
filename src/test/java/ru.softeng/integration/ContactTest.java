package ru.softeng.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.softeng.form.ContactForm;
import ru.softeng.model.Contact;
import ru.softeng.repository.IContactRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class ContactTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IContactRepository repository;
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate template;

    @Before
    public void beforeMethod() {
        this.template = new JdbcTemplate(dataSource);
    }

    private ObjectMapper jsonMapper = new ObjectMapper();

    private static final String IMG = "https://pp.userapi.com/c854532/v854532436/45a0d/YtQ0UQlVh6U.jpg";
    private static final String NAME = "Андрей";
    private static final String PHONE = "89001259871";
    private static final String DESC = "bboy Taiko";

    private static final String IMG_EDITED = "https://upload.wikimedia.org/wikipedia/commons/4/43/%D0%A7%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D1%87%D0%B5%D0%BA.png";
    private static final String NAME_EDITED = "Не Андрей";
    private static final String PHONE_EDITED = "89001259871";
    private static final String DESC_EDITED = "bboy Lipton";

    private static final String SELECT_ID_BY_NAME_SQL = "SELECT id FROM contacts WHERE name=?";

    private UUID getID(String name) {
        return UUID.fromString(template.queryForObject(SELECT_ID_BY_NAME_SQL,
                (ResultSet resultSet, int i) -> (resultSet.getString("id")), name));
    }

    private void assertResultActions(ResultActions resultActions) {
        Assert.assertNotNull(resultActions);
        Assert.assertNotNull(resultActions.andReturn());
        Assert.assertNotNull(resultActions.andReturn().getResponse());
    }

    private void assertResponseOnEdit(ContactForm contactForm, boolean expectedResult, ContactForm expect) throws Exception {
        UUID id = getID(NAME);

        ResultActions resultActions =
                this.mockMvc.perform(put("/contacts/{id}/edit", id)
                        .content(jsonMapper.writeValueAsString(contactForm))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertResultActions(resultActions);
        Assert.assertEquals(resultActions.andReturn().getResponse().getContentAsString(), String.valueOf(expectedResult));

        Contact c = repository.getByID(id);

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getImage(), expect.getImage());
        Assert.assertEquals(c.getName(), expect.getName());
        Assert.assertEquals(c.getPhone(), expect.getPhone());
        Assert.assertEquals(c.getDescription(), expect.getDescription());
    }

    private void assertResponseOnSave(ContactForm contactForm, boolean expectedResult) throws Exception {
        ResultActions resultActions =
                this.mockMvc.perform(post("/contacts/save")
                        .content(jsonMapper.writeValueAsString(contactForm))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertResultActions(resultActions);
        Assert.assertEquals(resultActions.andReturn().getResponse().getContentAsString(), String.valueOf(expectedResult));

        if(expectedResult) {
            UUID id = getID(NAME);
            Assert.assertNotNull(id);
            Contact c = repository.getByID(id);
            Assert.assertNotNull(c);

            Assert.assertEquals(c.getImage(), IMG);
            Assert.assertEquals(c.getName(), NAME);
            Assert.assertEquals(c.getPhone(), PHONE);
            Assert.assertEquals(c.getDescription(), DESC);
        } else {
            Assert.assertTrue(repository.getAll().isEmpty());
        }
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getRecordFromListTest() throws Exception {
        this.mockMvc.perform(get("/contacts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/table/tbody/tr[1]/td[2]").string(NAME))
                .andExpect(xpath("/html/body/table/tbody/tr[1]/td[3]").string(DESC))
                .andExpect(xpath("/html/body/table/tbody/tr[1]/td[4]").string(PHONE));
        //*The image test got to be here*
    }

    @Test
    @Sql(value = {"/create-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putContactWithTrueParams() throws Exception {
        ContactForm contactForm = new ContactForm(NAME, IMG, DESC, PHONE);
        assertResponseOnSave(contactForm, true);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putContactWithIncorrectName() throws Exception {
        ContactForm contactForm = new ContactForm(" ", IMG, DESC, PHONE);
        assertResponseOnSave(contactForm, false);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putContactWithIncorrectPhone() throws Exception {
        ContactForm contactForm = new ContactForm(NAME, IMG, DESC, "123");
        assertResponseOnSave(contactForm, false);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putContactWithExistsPhone() throws Exception {
        ContactForm contactForm = new ContactForm(NAME, IMG, DESC, "333-333");

        ResultActions resultActions =
                this.mockMvc.perform(post("/contacts/save")
                        .content(jsonMapper.writeValueAsString(contactForm))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertResultActions(resultActions);
        Assert.assertEquals(repository.getAll().size(), 1);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putEditedContactWithTrueParams() throws Exception {
        ContactForm contactForm = new ContactForm(NAME, IMG, DESC, PHONE);
        assertResponseOnEdit(contactForm, true, contactForm);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putEditedContactWithIncorrectName() throws Exception {
        ContactForm contactForm = new ContactForm(" ", IMG_EDITED, DESC_EDITED, PHONE_EDITED);
        ContactForm expect = new ContactForm(NAME, IMG, DESC, PHONE);
        assertResponseOnEdit(contactForm, false, expect);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putEditedContactWithIncorrectPhone() throws Exception {
        ContactForm contactForm = new ContactForm(NAME_EDITED, IMG_EDITED, DESC_EDITED, "123");
        ContactForm expect = new ContactForm(NAME, IMG, DESC, PHONE);
        assertResponseOnEdit(contactForm, false, expect);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putEditedContactWithIncorrectPhone2() throws Exception {
        ContactForm contactForm = new ContactForm(NAME_EDITED, IMG_EDITED, DESC_EDITED, "АБВГДЕЁЖЗ");
        ContactForm expect = new ContactForm(NAME, IMG, DESC, PHONE);
        assertResponseOnEdit(contactForm, false, expect);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putEditedContactWithIncorrectImage() throws Exception {
        ContactForm contactForm = new ContactForm(NAME_EDITED, "Не url", DESC_EDITED, PHONE_EDITED);
        ContactForm expect = new ContactForm(NAME_EDITED, null, DESC_EDITED, PHONE_EDITED);
        assertResponseOnEdit(contactForm, true, expect);
    }

    @Test
    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql", "/create-test-contact2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void putEditedContactWithExistsPhone() throws Exception {
        ContactForm contactForm = new ContactForm(NAME_EDITED, IMG_EDITED, DESC_EDITED, "333-333");
        ContactForm expect = new ContactForm(NAME, IMG, DESC, PHONE);
        assertResponseOnEdit(contactForm, false, expect);
    }

//    @Test
//    @Sql(value = {"/create-contact-table.sql", "/create-test-contact.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(value = {"/delete-contact-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void incorrectIdOnEdit() throws Exception{
//        this.mockMvc.perform(get("/contacts/bd1e3991-1b77-46c8-8877-48471d714179/edit"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/contacts"));
//    }
}