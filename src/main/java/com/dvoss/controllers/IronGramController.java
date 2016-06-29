package com.dvoss.controllers;

import com.dvoss.entities.Photo;
import com.dvoss.entities.User;
import com.dvoss.services.PhotoRepository;
import com.dvoss.services.UserRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;

/**
 * Created by Dan on 6/28/16.
 */
@Controller
public class IronGramController {
    @Autowired
    UserRepository users;
    @Autowired
    PhotoRepository photos;

    Server dbui = null;

    @PostConstruct
    public void init() throws SQLException {
        dbui = Server.createWebServer().start();
    }
    @PreDestroy
    public void destroy() {
        dbui.stop();
    }


    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String receiver, HttpSession session, boolean isPublic, Long time) throws Exception {
        String username = (String) session.getAttribute("username");
        User sender = users.findFirstByName(username);
        User rec = users.findFirstByName(receiver);

        if (sender == null || rec == null) {
            throw new Exception("Cannot find sender or receiver.");
        }

        File dir = new File("public/photos");
        dir.mkdirs();

        if (!(file.getContentType().startsWith("image"))) {
            throw new Exception("Only pictures may be uploaded.");
        }
        else {
            File photoFile = File.createTempFile("photo", file.getOriginalFilename(), dir);
            FileOutputStream fos = new FileOutputStream(photoFile);
            fos.write(file.getBytes());

            Photo photo = new Photo(sender, rec, photoFile.getName(), time, isPublic);
            photos.save(photo);
        }
        return "redirect:/";
    }
}

