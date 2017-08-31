package ru.matmatch.search.repository;

import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;
import ru.matmatch.search.model.UserIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * solr crud repository don't support multicore queries out of the box.
 * So if you need multi core that's the way to go
 * https://stackoverflow.com/questions/20872393/how-to-implement-custom-solr-repository-using-spring-data-solr-for-multiple-core
 *
 * */
public class UserSearchRepositoryCustomImpl implements UserSearchrepositoryCustom {
    private SolrTemplate usersTemplate;

    public UserSearchRepositoryCustomImpl(SolrTemplate usersTemplate) {
        this.usersTemplate = usersTemplate;
    }


    @Override
    public List<UserIndex> findByFirstNameOrLastNameOrIpAddress(String searchTerm) {
        ArrayList<UserIndex> result = new ArrayList<>();
        try {
            Cursor<UserIndex> cursor = usersTemplate.queryForCursor(new SimpleQuery(String.format("firstName:%s OR lastName:%s OR ipAddress:%s",searchTerm,searchTerm,searchTerm)), UserIndex.class);
            cursor = cursor.isOpen() ? cursor : cursor.open();
            cursor.forEachRemaining(result::add);
            if (!cursor.isClosed())
                cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
