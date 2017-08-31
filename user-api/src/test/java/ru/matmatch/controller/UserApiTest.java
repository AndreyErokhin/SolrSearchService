package ru.matmatch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.matmatch.TestUtil;
import ru.matmatch.model.Gender;
import ru.matmatch.model.User;
import ru.matmatch.search.service.SearchService;
import ru.matmatch.storage.service.UserStorageService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by erokhin.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = SearchServiceTestConfiguration.class)
public class UserApiTest {

    MockMvc mockMvc;
    @Autowired
    private UserStorageService storage;
    @Autowired
    SearchService search;
    ObjectMapper mapper = new ObjectMapper();

    User first = new User("Andrey", "Erokhin", "andreye@mail.se", Gender.male, "localhost");
    User second = new User("Irina", "Ololoeva", "irina@google.info", Gender.female, "8.8.8.8");


    @Before
    public void beforeTest() throws Exception {
        storage.deleteAllUsers();
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(storage, search)).build();
        //empty storage
        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isNoContent());
    }

    private void createUserAndVerifyCode(User user, HttpStatus status) throws Exception {
        mockMvc.perform(post("/api/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().is(status.value()));
    }

    private List<User> getUsersList() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/"))
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }

    @Test
    public void testInsertAndListUsers() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);
        createUserAndVerifyCode(second, HttpStatus.CREATED);

        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Andrey")))
                .andExpect(jsonPath("$[0].lastName", is("Erokhin")))
                .andExpect(jsonPath("$[0].email", is("andreye@mail.se")))
                .andExpect(jsonPath("$[0].ipAddress", is("localhost")))
                .andExpect(jsonPath("$[0].gender", is("male")))

                .andExpect(jsonPath("$[1].firstName", is("Irina")))
                .andExpect(jsonPath("$[1].lastName", is("Ololoeva")))
                .andExpect(jsonPath("$[1].email", is("irina@google.info")))
                .andExpect(jsonPath("$[1].ipAddress", is("8.8.8.8")))
                .andExpect(jsonPath("$[1].gender", is("female")));
    }
    

    @Test
    public void testSelectById() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);

        List<User> users = getUsersList();

        createUserAndVerifyCode(second, HttpStatus.CREATED);

        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        //here we have the "first" user
        mockMvc.perform(get("/api/user/"+users.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("firstName", is("Andrey")))
                .andExpect(jsonPath("lastName", is("Erokhin")))
                .andExpect(jsonPath("email", is("andreye@mail.se")))
                .andExpect(jsonPath("ipAddress", is("localhost")))
                .andExpect(jsonPath("gender", is("male")));

    }

    @Test
    public void testInsertUsersWithSameEmail() throws Exception{
        createUserAndVerifyCode(first, HttpStatus.CREATED);

        mockMvc.perform(post("/api/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(first)))
                .andExpect(status().isConflict());

    }

    @Test
    public void testUpdateUser() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);

        List<User> users = getUsersList();

        createUserAndVerifyCode(second, HttpStatus.CREATED);

        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        //here we have the "first" user
        User updatedFirst=users.get(0);
        updatedFirst.setEmail("new@emaul.ru");
        mockMvc.perform(put("/api/user/"+updatedFirst.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedFirst)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Andrey")))
                .andExpect(jsonPath("$[0].lastName", is("Erokhin")))
                .andExpect(jsonPath("$[0].email", is("new@emaul.ru")))
                .andExpect(jsonPath("$[0].ipAddress", is("localhost")))
                .andExpect(jsonPath("$[0].gender", is("male")))

                .andExpect(jsonPath("$[1].firstName", is("Irina")))
                .andExpect(jsonPath("$[1].lastName", is("Ololoeva")))
                .andExpect(jsonPath("$[1].email", is("irina@google.info")))
                .andExpect(jsonPath("$[1].ipAddress", is("8.8.8.8")))
                .andExpect(jsonPath("$[1].gender", is("female")));
    }


    @Test
    public void testDeleteUser() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);

        List<User> users = getUsersList();

        createUserAndVerifyCode(second, HttpStatus.CREATED);

        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        //here we have the "first" user
        mockMvc.perform(delete("/api/user/" + users.get(0).getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }
    

    @Test
    public void testDeleteAllUsers() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);
        createUserAndVerifyCode(second, HttpStatus.CREATED);


        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(delete("/api/user/"))
                .andExpect(status().isNoContent());

        Assert.assertTrue(mockMvc.perform(get("/api/user/"))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString().isEmpty());
    }
}
