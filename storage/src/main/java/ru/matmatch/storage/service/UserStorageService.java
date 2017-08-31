package ru.matmatch.storage.service;


import java.util.List;
import ru.matmatch.model.User;


public interface UserStorageService {

    public List<User> findAllUsers();

    public List<User> findAllUsers(Iterable<Long> ids);

    public User findById(long id);

    public boolean isUserExist(User user);

    public void saveUser(User user);

    public void saveAll(Iterable<User> users);

    public void updateUser(User currentUser);

    void deleteUser(long id);

    void deleteAllUsers();
}
