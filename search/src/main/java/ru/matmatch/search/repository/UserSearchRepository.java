package ru.matmatch.search.repository;

import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import ru.matmatch.search.model.UserIndex;

import java.util.List;

public interface UserSearchRepository extends SolrCrudRepository<UserIndex,Long> {


    @Query("firstName:*?0* OR lastName:*?0* OR ipAddress:*?0*")
    List<UserIndex> findByFirstNameOrLastNameOrIpAddress(String searchTerm);

}
