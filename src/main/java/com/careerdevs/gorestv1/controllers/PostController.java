package com.careerdevs.gorestv1.controllers;


import com.careerdevs.gorestv1.model.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    Environment env;

    @GetMapping("/{id}")
    public Object getOneUser(
            @PathVariable("id") String userId,
            RestTemplate restTemplate
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/posts/" + userId;
            String token = env.getProperty("GOREST_TOKEN");
            url+="?access-token=" + token;

            var user =restTemplate.getForObject(url, PostModel.class);
            assert user != null;
            System.out.println("Repot: \n" + user.generateReport());

            return user;
        } catch (Exception e) {
            return "404:No user exist with the ID" + userId;
        }

    }

//    @DeleteMapping("/{id}")
//    public Object deleteOneUser(
//
//    )
}
