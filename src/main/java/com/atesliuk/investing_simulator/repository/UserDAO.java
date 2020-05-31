package com.atesliuk.investing_simulator.repository;

import com.atesliuk.investing_simulator.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Long> {
}
