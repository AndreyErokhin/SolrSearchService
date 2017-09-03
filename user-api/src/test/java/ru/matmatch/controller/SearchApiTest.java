package ru.matmatch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.matmatch.TestUtil;
import ru.matmatch.model.Gender;
import ru.matmatch.model.User;
import ru.matmatch.search.service.SearchService;
import ru.matmatch.storage.service.UserStorageService;

import java.util.List;
import java.util.function.Predicate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by erokhin on 9/2/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = SearchServiceTestConfiguration.class)
public class SearchApiTest {

    MockMvc mockMvc;

    @Autowired
    private UserStorageService storage;

    @Autowired
    SearchService search;
    ObjectMapper mapper = new ObjectMapper();

    User first = new User("Andrey", "Erokhin", "andreye@mail.se", Gender.male, "localhost");
    User second = new User("Petr", "Karasev", "karas@google.info", Gender.male, "remotehost");

    @Before
    public void beforeTest() throws Exception {
        //start with clear index
        storage.deleteAllUsers();
        search.deleteAllFromIndex();

        mockMvc = MockMvcBuilders.standaloneSetup(new SearchController(search), new UserController(storage, search)).build();
    }

    private void createUserAndVerifyCode(User user, HttpStatus status) throws Exception {
        mockMvc.perform(post("/api/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().is(status.value()));
    }

    private void updateUserAndVerifyCode(User user, HttpStatus status) throws Exception {
        mockMvc.perform(put("/api/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().is(status.value()));
    }

    private List<User> searchUsers(String searchText, HttpStatus status) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/search?searchText=" + searchText))
                .andExpect(status().is(status.value()))
                .andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, User.class));

    }

    private List<User> searchAndVerify(String searchText, int expectedCnt, HttpStatus expectedHttpStatus, Predicate<User> verifier) throws Exception {
        List<User> results = searchUsers(searchText, expectedHttpStatus);
        Assert.assertEquals("Incorrect count returned", expectedCnt, results.size());
        results.forEach(user -> Assert.assertTrue(verifier.test(user)));
        return results;
    }

    @Test
    public void usersAreSearchebleTest() throws Exception {
        searchUsers("Ero", HttpStatus.NO_CONTENT);

        createUserAndVerifyCode(first, HttpStatus.CREATED);

        searchAndVerify("Ero", 1, HttpStatus.OK, user -> user.getLastName().contains("Ero"));
        searchAndVerify("khi", 1, HttpStatus.OK, user -> user.getLastName().contains("khi"));
        searchAndVerify("in", 1, HttpStatus.OK, user -> user.getLastName().contains("in"));

        searchAndVerify("Andrey", 1, HttpStatus.OK, user -> user.getFirstName().contains("Andrey"));

        searchAndVerify("local", 1, HttpStatus.OK, user -> user.getFirstName().contains("Andrey") && user.getIpAddress().contains("local"));

        searchAndVerify("Matmatch", 0, HttpStatus.NO_CONTENT, user -> true);

        createUserAndVerifyCode(second, HttpStatus.CREATED);
        searchAndVerify("host", 2, HttpStatus.OK, user -> user.getIpAddress().contains("host"));

    }

    @Test
    public void shouldntDuplicateOnUpdate() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);
        User firstCopy=searchAndVerify("Andrey", 1, HttpStatus.OK, user -> user.getFirstName().contains("Andrey")).get(0);
        createUserAndVerifyCode(second, HttpStatus.CREATED);
        searchAndVerify("host", 2, HttpStatus.OK, user -> user.getIpAddress().contains("host"));

        firstCopy.setFirstName("Petras");
        updateUserAndVerifyCode(firstCopy,HttpStatus.OK);

        searchAndVerify("Petr", 2, HttpStatus.OK, user -> user.getFirstName().contains("Petr"));

    }


    @Test
    public void testCaseInsensitive() throws Exception {
        createUserAndVerifyCode(first, HttpStatus.CREATED);
        searchAndVerify("Ero", 1, HttpStatus.OK, user -> user.getLastName().contains("Ero"));
        searchAndVerify("ero", 1, HttpStatus.OK, user -> user.getLastName().contains("Ero"));
        searchAndVerify("KHI", 1, HttpStatus.OK, user -> user.getLastName().contains("khi"));
        searchAndVerify("khi", 1, HttpStatus.OK, user -> user.getLastName().contains("khi"));

    }
}
