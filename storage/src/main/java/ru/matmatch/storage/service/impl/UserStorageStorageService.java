package ru.matmatch.storage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmatch.model.User;
import ru.matmatch.storage.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserStorageStorageService implements ru.matmatch.storage.service.UserStorageService {

    UserRepository repository;

    @Autowired
    public UserStorageStorageService(UserRepository repository){
        this.repository=repository;
    }

    @Override
    public List<User> findAllUsers() {
        Iterable<User> iterable = repository.findAll();
        List<User> target = new ArrayList<User>();
        iterable.forEach(target::add);
        return target;
    }

    @Override
    public List<User> findAllUsers(Iterable<Long> ids) {
        Iterable<User> iterable = repository.findAll(ids);
        List<User> target = new ArrayList<User>();
        iterable.forEach(target::add);
        return target;
    }

    @Override
    public User findById(long id) {
        return repository.findOne(id);
    }

    @Override
    public boolean isUserExist(User user) {
        return repository.findByEmail(user.getEmail())!=null;
    }

    @Override
    public void saveUser(User user) {
        repository.save(user);
    }

    @Override
    public void saveAll(Iterable<User> users) {
        repository.save(users);
    }

    @Override
    public void updateUser(User currentUser) {
        saveUser(currentUser);
    }

    @Override
    public void deleteUser(long id) {
        repository.delete(id);
    }

    @Override
    public void deleteAllUsers() {
        repository.deleteAll();
    }
}
