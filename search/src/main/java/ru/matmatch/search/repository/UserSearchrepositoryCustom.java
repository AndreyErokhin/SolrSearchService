package ru.matmatch.search.repository;

import org.springframework.data.solr.repository.Query;
import ru.matmatch.search.model.UserIndex;

import java.util.List;

public interface UserSearchrepositoryCustom {
    List<UserIndex> findByFirstNameOrLastNameOrIpAddress(String searchTerm);
}
