package ru.matmatch;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import ru.matmatch.dataloader.CsvDataLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableSolrRepositories
public class MatMatchApp {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MatMatchApp.class);
//        CsvDataLoader loader = new CsvDataLoader();
//        String classPath=ClassPath.getClassPath();
//        ClassPathResource res = new ClassPathResource("classpath:example.csv");
//        String path =res.getPath();
//        File csv = new File(path);
//        loader.loadDataFromFile(csv);
    }
}
