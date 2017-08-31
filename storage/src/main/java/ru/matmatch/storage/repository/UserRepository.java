package ru.matmatch.storage.repository;

import org.springframework.data.repository.CrudRepository;
import ru.matmatch.model.Gender;
import ru.matmatch.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    public List<User> findByLastName(String lastName);

    public List<User> findByFirstName(String firstName);

    public User findByEmail(String email);

    public List<User> findByGender(Gender gender);

    public List<User> findByIpAddress(String ipAddress);
}
