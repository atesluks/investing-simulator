package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * DAO class, supports CRUD operations with User objects.
 * Also, have additional query method.
 */
@CrossOrigin(origins = "http://localhost:4200")
public interface UserDAO extends CrudRepository<User, Long> {

    /**
     * The method returns a list of users that match provided email and password
     * @param email - a String object, a user's email
     * @param password - a String object, a user's password
     * @return - list of User objects that match provided email and password
     */
    @Query(value = "SELECT * FROM Users u WHERE u.email = :email AND u.password = :password", nativeQuery = true)
    List<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

}
