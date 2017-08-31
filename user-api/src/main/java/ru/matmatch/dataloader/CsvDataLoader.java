package ru.matmatch.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.matmatch.dataloader.parser.UserParser;
import ru.matmatch.model.User;
import ru.matmatch.search.service.SearchService;
import ru.matmatch.storage.service.UserStorageService;

import java.io.*;
import java.util.List;

/**
 * Created by erokhin.
 */
@Component
public class CsvDataLoader implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(CsvDataLoader.class);
    @Autowired
    UserStorageService storage;
    @Autowired
    SearchService search;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = new ClassPathResource("/example.csv").getInputStream();
        UserParser parser = new UserParser();
        List<User>users=parser.parseInputStream(is);
        storage.saveAll(users);
        search.addAllToIndex(users);
    }
}
