package com.dvoss.services;

import com.dvoss.entities.Photo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dan on 6/28/16.
 */
public interface PhotoRepository extends CrudRepository<Photo, Integer> {
}
