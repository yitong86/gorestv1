package com.careerdevs.gorestv1.controllers;


import com.careerdevs.gorestv1.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    Environment env;

    //URL/endpoint http://localhost:4444/api/user/token
    @GetMapping("/token")
    public String getToken() {
        return env.getProperty("GOREST_TOKEN");
    }

    //URL/endpoint http://localhost:4444/api/user/{id}
    @GetMapping("/{id}")
    public Object getOneUser(
            @PathVariable("id") String userId,
            RestTemplate restTemplate
    ) {
        try {
            String url = "https://gorest.co.in/public/v2/users/" + userId;
            String apiToken = env.getProperty("GOREST_TOKEN");
            //add query param
            url += "?access-token=" + apiToken;
            return restTemplate.getForObject(url, Object.class);

            //Manual approach
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(env.getProperty("GOREST_TOKEN"));
//            HttpEntity request = new HttpEntity(headers);
//
//            return restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    request,
//                    Object.class
//            );


        } catch (Exception exception) {
            return "404:No user exist with the ID" + userId;
        }
    }

    @DeleteMapping("/{id}")
    public Object deleteOneUser(
            @PathVariable("id") String userId,
            RestTemplate restTemplate
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/users/" + userId;
            String token = env.getProperty("GOREST_TOKEN");

            url += "?access-token=" + token;

            var user = restTemplate.getForObject(url, UserModel.class);
            assert user != null;
            System.out.println("Repot: \n" + user.generateReport());

            return user;

        } catch (HttpClientErrorException.NotFound exception) {
            return "User could not be delete, user #" + userId + "does not exist";
        } catch (HttpClientErrorException.Unauthorized exception) {
            return "You are not authorized to delete user #" + userId;

        } catch (Exception exception) {
            System.out.println(exception.getClass());
            return exception.getMessage();
        }
    }

    @PostMapping("/")
    public Object postUserQueryParam(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("gender") String gender,
            @RequestParam("status") String status,
            RestTemplate restTemplate
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/users/";
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            UserModel newUser = new UserModel(name, email, gender, status);

            //TODO: validate that gender and status are valid before continuing
            //TODO: validate that the email is a valid email

            System.out.println("Date to be sent:\n" + newUser);

            HttpEntity<UserModel> request = new HttpEntity<>(newUser);

            return restTemplate.postForEntity(url, request, UserModel.class);

        } catch (Exception exception) {
            System.out.println(exception.getClass());
            return exception.getMessage();
        }
    }


    @PostMapping("/")
    public ResponseEntity postUser(
            RestTemplate restTemplate,
            @RequestBody UserModel newUser
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/users/";
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            HttpEntity<UserModel> request = new HttpEntity<>(newUser);

            return restTemplate.postForEntity(url, request, UserModel.class);
            // return new ResponseEntity<>("Success!",HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getClass() + "\n" + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

