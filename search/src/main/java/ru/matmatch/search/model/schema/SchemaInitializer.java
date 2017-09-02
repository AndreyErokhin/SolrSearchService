package ru.matmatch.search.model.schema;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

/**
 * Created by erokhin on 9/2/2017.
 */
public interface SchemaInitializer {
    public void initSchema() throws IOException, SolrServerException;
}
