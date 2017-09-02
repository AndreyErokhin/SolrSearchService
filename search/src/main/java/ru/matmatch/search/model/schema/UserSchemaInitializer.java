package ru.matmatch.search.model.schema;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by erokhin on 9/2/2017.
 */
@Component
public class UserSchemaInitializer implements SchemaInitializer {

    public static class USER_SCHEMA_DESCRIPTION {
        public static final String SOLR_CORE = "userIndex";

        public static class FIELDS {
            public static final String FIRST_NAME = "firstName";
            public static final String LAST_NAME = "lastName";
            public static final String IPADDRESS = "ipAddress";
        }
    }

    @Autowired
    private SolrClient client;

    @Override
    public void initSchema() throws IOException, SolrServerException {
        createSchemaField(USER_SCHEMA_DESCRIPTION.SOLR_CORE, USER_SCHEMA_DESCRIPTION.FIELDS.FIRST_NAME, "text_general", false, true);
        createSchemaField(USER_SCHEMA_DESCRIPTION.SOLR_CORE, USER_SCHEMA_DESCRIPTION.FIELDS.LAST_NAME, "text_general", false, true);
        createSchemaField(USER_SCHEMA_DESCRIPTION.SOLR_CORE, USER_SCHEMA_DESCRIPTION.FIELDS.IPADDRESS, "text_general", false, true);
    }

    private SchemaRequest.AddField createAddFielderequest(String fieldName, String fieldType, boolean stored, boolean indexed) {
        Map<String, Object> fieldAttributes = new LinkedHashMap<>();
        fieldAttributes.put("name", fieldName);
        fieldAttributes.put("type", fieldType);
        fieldAttributes.put("stored", stored);
        fieldAttributes.put("indexed", indexed);
        return new SchemaRequest.AddField(fieldAttributes);
    }

    private void createSchemaField(String solrCore, String fieldName, String fieldType, boolean stored, boolean indexed) throws SolrServerException, IOException {
        SchemaRequest.AddField addFieldRequest = createAddFielderequest(fieldName, fieldType, stored, indexed);
        SchemaResponse.UpdateResponse response=addFieldRequest.process(client/*, solrCore*/);
        client.commit();
        response.getResponse();
    }
}
