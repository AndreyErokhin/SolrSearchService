package ru.matmatch.search.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.HttpSolrClientFactory;
import ru.matmatch.search.model.UserIndex;

import java.util.List;

@Configuration
@EnableSolrRepositories(value = "ru.matmatch.search.repository", multicoreSupport = true, schemaCreationSupport = true)
@ComponentScan
public class SolrConfig {


    @Bean
    public SolrClient solrClient(String core) {
        HttpSolrClient client = new HttpSolrClient("http://172.18.0.22:8983/solr/");
        SolrClientFactory factory = new HttpSolrClientFactory(client);
        return factory.getSolrClient(core);
    }

    @Bean()
    public SolrTemplate solrTemplate() throws Exception {
        String coreName="userIndex";
        return new SolrTemplate(solrClient(coreName),coreName);
    }



}
