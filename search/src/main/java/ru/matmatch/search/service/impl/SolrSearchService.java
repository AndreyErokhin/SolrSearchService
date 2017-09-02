package ru.matmatch.search.service.impl;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmatch.model.User;
import ru.matmatch.search.Utils;
import ru.matmatch.search.model.UserIndex;
import ru.matmatch.search.model.schema.SchemaInitializer;
import ru.matmatch.search.repository.UserSearchRepository;
import ru.matmatch.search.service.SearchService;
import ru.matmatch.storage.service.UserStorageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolrSearchService implements SearchService<User, Long> {

    @Autowired
    private SchemaInitializer schemaInitializer;
    private UserSearchRepository repository;
    private UserStorageService storage;

    @Autowired
    public SolrSearchService(UserSearchRepository repository, UserStorageService storage) {
        this.repository = repository;
        this.storage = storage;
    }

    @PostConstruct
    private void createSolrSchema() throws IOException, SolrServerException {
        schemaInitializer.initSchema();
    }

    @Override
    public void addToIndex(User user) {
        repository.save(Utils.userToIndexMapper.apply(user));
    }

    @Override
    public void addAllToIndex(Collection<User> users) {
        repository.save(users.stream().map(Utils.userToIndexMapper).collect(Collectors.toList()));
    }

    @Override
    public void deleteFromIndex(User user) {
        repository.delete(Utils.userToIndexMapper.apply(user));
    }

    @Override
    public void deleteFromIndexById(Long id) {
        repository.delete(id);
    }

    @Override
    public void deleteAllFromIndex(Collection<User> documents) {
        repository.delete(documents.stream()
                .map(Utils.userToIndexMapper)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void deleteAllFromIndex() {
        repository.deleteAll();
    }

    @Override
    public List<User> fullTextSearch(String searchString) {
        List<UserIndex> indexObjects = repository.findByFirstNameOrLastNameOrIpAddress(searchString);
        List<Long> userIds = indexObjects.stream()
                .map(userIndex -> userIndex.getId())
                .collect(Collectors.toList());
        return storage.findAllUsers(userIds);
    }
}
