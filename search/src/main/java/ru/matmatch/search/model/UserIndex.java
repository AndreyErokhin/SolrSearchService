package ru.matmatch.search.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "userIndex")
public class UserIndex {
    @Id
    private Long id;

    @Indexed(type = "text_general")
    private String firstName;

    @Indexed(type = "text_general")
    private String lastName;

    @Indexed(type = "text_general")
    private String ipAddress;



    public UserIndex(Long id, String firstName, String lastName, String ipAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ipAddress = ipAddress;
    }



    public Long getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
