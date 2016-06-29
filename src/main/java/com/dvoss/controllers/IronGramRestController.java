package com.dvoss.controllers;

import com.dvoss.entities.Photo;
import com.dvoss.entities.User;
import com.dvoss.services.PhotoRepository;
import com.dvoss.services.UserRepository;
import com.dvoss.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dan on 6/28/16.
 */
@RestController
public class IronGramRestController {
    @Autowired
    UserRepository users;
    @Autowired
    PhotoRepository photos;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(@RequestBody User user, HttpSession session, HttpServletResponse response) throws Exception {
        User userFromDB = users.findFirstByName(user.getName());
        if (userFromDB == null) {
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(user.getPassword(), userFromDB.getPassword())) {
            throw new Exception("Wrong password.");
        }
        session.setAttribute("username", user.getName());
        response.sendRedirect("/");
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
    }

    @RequestMapping(path = "/photos", method = RequestMethod.GET)
    public Iterable<Photo> getPhotos(HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);

        Iterable<Photo> p = photos.findByRecipient(user);
        for (Photo photo : p) {
            if (photo.getDt() == null) {
                photo.setDt(LocalDateTime.now());
                photos.save(photo);
            }
            else if (LocalDateTime.now().isAfter(photo.getDt().plusSeconds(photo.getTime()))) {
                photos.delete(photo);
                File fileOnDisk = new File("public/photos/" + photo.getFilename());
                fileOnDisk.delete();
            }
        }
        return photos.findByRecipient(user);
    }

    // this route is faulty:
    @RequestMapping(path = "/public-photos", method = RequestMethod.GET)
    public Iterable<Photo> getPublicPhotos(String username) {
        User user = users.findFirstByName(username);
        return photos.findByIsPublicTrue(user);
    }
}
