package com.dvoss.services;

import com.dvoss.entities.Photo;
import com.dvoss.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dan on 6/28/16.
 */
public interface PhotoRepository extends CrudRepository<Photo, Integer> {
    public Iterable<Photo> findByRecipient(User recipient);
}
