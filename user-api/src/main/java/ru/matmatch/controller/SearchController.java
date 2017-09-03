package ru.matmatch.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matmatch.model.User;
import ru.matmatch.search.service.SearchService;

import java.util.List;


@RestController
@RequestMapping("/api/search")
@Api(value = "search", description = "Full text search API.")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Full text search for users based on first name, second name and ip-address", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved users list"),
            @ApiResponse(code = 204, message = "No users found with the specified search criteria")
    }
    )
    public ResponseEntity<?> searchUsers(@RequestParam("searchText") String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        logger.info("Searching for Users using search text {}", searchText);
        List<User> users = searchService.fullTextSearch(searchText);
        return new ResponseEntity<List<User>>(users, users.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }
}
