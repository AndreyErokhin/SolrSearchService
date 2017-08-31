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

    @Autowired
    SearchService searchService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> searchUsers(@RequestParam("searchText") String searchText) {
        logger.info("Searching for Users using search text {}", searchText);
        List<User> user = searchService.fullTextSearch(searchText);
        return new ResponseEntity<List<User>>(user, HttpStatus.OK);
    }

}
