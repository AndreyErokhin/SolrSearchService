package ru.matmatch.search.service;

import java.util.Collection;
import java.util.List;

public interface SearchService<T, ID> {
    public void addToIndex(T document);
    public void addAllToIndex(Collection<T> documents);
    public void deleteFromIndex(T document);
    public void deleteFromIndexById(ID id);
    public void deleteAllFromIndex(Collection<T> documents);
    public void deleteAllFromIndex();
    public List<T> fullTextSearch(String searchString);
}
