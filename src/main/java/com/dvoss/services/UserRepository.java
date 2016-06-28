package com.dvoss.services;

import com.dvoss.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dan on 6/28/16.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    public User findFirstByName(String name);
}
