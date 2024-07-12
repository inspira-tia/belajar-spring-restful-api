package tia_sarwoedhi.belajar_spring_restful_api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tia_sarwoedhi.belajar_spring_restful_api.entity.Contact;
import tia_sarwoedhi.belajar_spring_restful_api.entity.User;
import tia_sarwoedhi.belajar_spring_restful_api.model.ContactResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.WebResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.CreateContactRequest;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.UpdateContactRequest;
import tia_sarwoedhi.belajar_spring_restful_api.repository.ContactRepository;
import tia_sarwoedhi.belajar_spring_restful_api.repository.UserRepository;
import tia_sarwoedhi.belajar_spring_restful_api.security.BCrypt;

import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpireAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactFailedNotFound() throws Exception {

        Contact contact = new Contact();
        contact.setId("123");
        contact.setFirstName("Tia");
        contact.setEmail("Tia@mail.com");
        contact.setPhone("1231314141");
        contact.setLastName("Sarwoedhi");
        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        contact.setUser(userDB);

        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/4214141")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactSuccess() throws Exception {

        Contact contact = new Contact();
        contact.setId("123");
        contact.setFirstName("Tia");
        contact.setEmail("Tia@mail.com");
        contact.setPhone("1231314141");
        contact.setLastName("Sarwoedhi");
        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        contact.setUser(userDB);

        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });
            assertEquals("1231314141", response.getData().getPhone());
        });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Tia");
        request.setEmail("Tia@mail.com");
        request.setPhone("1231314141");
        request.setLastName("Sarwoedhi");

        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });

            assertEquals("Tia@mail.com", response.getData().getEmail());

            assertTrue(contactRepository.existsById(response.getData().getId()));

        });
    }

    @Test
    void updateContactBadRequest() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        mockMvc.perform(
                put("/api/contacts/1234")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateContactSuccess() throws Exception {
        Contact contact = new Contact();
        contact.setId("123");
        contact.setFirstName("okto");
        contact.setEmail("oke@mail.com");
        contact.setPhone("445551");
        contact.setLastName("okay");
        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        contact.setUser(userDB);
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Tia sarwoedhi");
        request.setEmail("Tia@mail123.com");
        request.setPhone("1231314141");
        request.setLastName("Sarwoedhi");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });

            assertEquals("Tia@mail123.com", response.getData().getEmail());
            assertEquals("1231314141", response.getData().getPhone());

            assertTrue(contactRepository.existsById(response.getData().getId()));

        });
    }


    @Test
    void deleteContactFailedNotFound() throws Exception {

        Contact contact = new Contact();
        contact.setId("123");
        contact.setFirstName("Tia");
        contact.setEmail("Tia@mail.com");
        contact.setPhone("1231314141");
        contact.setLastName("Sarwoedhi");
        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        contact.setUser(userDB);

        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/4214141")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteContactSuccess() throws Exception {

        Contact contact = new Contact();
        contact.setId("123");
        contact.setFirstName("Tia");
        contact.setEmail("Tia@mail.com");
        contact.setPhone("1231314141");
        contact.setLastName("Sarwoedhi");
        User userDB = userRepository.findById("test").orElse(null);
        String token = userDB.getToken() != null ? userDB.getToken() : "";
        contact.setUser(userDB);

        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertEquals("Ok", response.getData());
        });
    }

    @Test
    void searchContactNotfound() throws Exception {
        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name","Tia")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(2, response.getData().size());
            assertEquals(1, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());

        });
    }

}