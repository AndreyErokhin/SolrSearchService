package ru.matmatch.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.matmatch.model.User;
import ru.matmatch.search.service.SearchService;
import ru.matmatch.storage.service.UserStorageService;

import java.util.List;


@RestController
@RequestMapping("/api")
@Api(value = "users CRUD", description = "Users CRUD API.")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserStorageService storageService;
    private SearchService<User,Long> searchService;

    @Autowired
    public UserController(UserStorageService storageService, SearchService<User, Long> searchService) {
        this.storageService = storageService;
        this.searchService = searchService;
    }

    @ApiOperation(value = "List all users available", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved users list"),
            @ApiResponse(code = 204, message = "No users available")
    }
    )
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = storageService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    @ApiOperation(value = "Get the user with the paricular ID", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "User with the specified id isn't found")
    }
    )
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        logger.info("Fetching User with id {}", id);
        User user = storageService.findById(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity(new ErrorType("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Add new user to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully retrieved user"),
            @ApiResponse(code = 409, message = "User with the specified email is already exists")
    }
    )
    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", user);

        if (storageService.isUserExist(user)) {
            logger.error("Unable to create. A User with email {} already exist", user.getEmail());
            return new ResponseEntity(new ErrorType("Unable to create. A User with email " +
                    user.getEmail() + " already exist."), HttpStatus.CONFLICT);
        }
        storageService.saveUser(user);
        searchService.addToIndex(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update information for the user with the spesified ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users successfully updated"),
            @ApiResponse(code = 404, message = "User with the specified ID is not found")
    }
    )
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        logger.info("Updating User with id {}", id);

        User currentUser = storageService.findById(id);

        if (currentUser == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new ErrorType("Unable to upate. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmail(user.getEmail());
        currentUser.setGender(user.getGender());
        currentUser.setIpAddress(user.getIpAddress());
        storageService.updateUser(currentUser);
        searchService.addToIndex(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }


    @ApiOperation(value = "Delete user with the spesified ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User successfully deleted"),
            @ApiResponse(code = 404, message = "User with the specified ID is not found")
    }
    )
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting User with id {}", id);

        User user = storageService.findById(id);
        if (user == null) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity(new ErrorType("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        storageService.deleteUser(id);
        searchService.deleteFromIndexById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete all users")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Users successfully deleted")
    }
    )
    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        logger.info("Deleting All Users");

        storageService.deleteAllUsers();
        searchService.deleteAllFromIndex();
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }


}
