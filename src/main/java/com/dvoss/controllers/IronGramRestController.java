package com.dvoss.controllers;

import com.dvoss.services.PhotoRepository;
import com.dvoss.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dan on 6/28/16.
 */
@RestController
public class IronGramRestController {
    @Autowired
    UserRepository users;
    @Autowired
    PhotoRepository photos;
}
