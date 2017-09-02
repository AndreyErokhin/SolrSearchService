package ru.matmatch.controller;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactory;
import ru.matmatch.MatMatchApp;

import java.nio.file.Paths;

/**
 * Created by erokhin.
 */
@Configuration
@Import(MatMatchApp.class)
public class SearchServiceTestConfiguration {
    private static final String CORE_NAME = "userIndex";
    //Override the client to start embedded instance of solr
    @Bean
    @Primary
    public SolrClient solrClient() throws Exception {
        CoreContainer coreContainer = CoreContainer.createAndLoad(Paths.get("src/test/resources/ru/matmatch/solr/").toAbsolutePath());
        EmbeddedSolrServer newServer = new EmbeddedSolrServer(coreContainer, CORE_NAME);
        return newServer;
    }

    @Bean
    @Primary
    public SolrOperations solrTemplate() throws Exception {
        return new SolrTemplate(solrClient(), CORE_NAME);
    }
}
