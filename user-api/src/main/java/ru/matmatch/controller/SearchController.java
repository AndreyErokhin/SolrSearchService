package ru.matmatch.controller;

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
@RequestMapping("/search")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> searchUsers(@RequestParam("searchText") String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        logger.info("Searching for Users using search text {}", searchText);
        List<User> users = searchService.fullTextSearch(searchText);
        return new ResponseEntity<List<User>>(users, users.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }
}
