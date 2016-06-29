package com.dvoss.services;

import com.dvoss.entities.Photo;
import com.dvoss.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dan on 6/28/16.
 */
public interface PhotoRepository extends CrudRepository<Photo, Integer> {
    public Iterable<Photo> findByRecipient(User recipient);
    List<Photo> findByIsPublicTrue(User sender);
}
