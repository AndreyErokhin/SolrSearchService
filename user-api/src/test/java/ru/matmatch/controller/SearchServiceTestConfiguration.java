package ru.matmatch.controller;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import ru.matmatch.MatMatchApp;
import ru.matmatch.search.service.SearchService;
import ru.matmatch.storage.service.UserStorageService;
import ru.matmatch.storage.service.impl.UserStorageStorageService;

/**
 * Created by erokhin.
 */
@Configuration
@Import(MatMatchApp.class)
public class SearchServiceTestConfiguration {
    //Override the searchService. Othervise the tests will not work without solr instance setup.
    @Bean
    @Primary
    public SearchService searchService() {
        return Mockito.mock(SearchService.class);
    }

    }
