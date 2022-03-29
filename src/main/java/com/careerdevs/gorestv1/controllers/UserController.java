package com.careerdevs.gorestv1.controllers;


import com.careerdevs.gorestv1.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

    ////URL/endpoint http://localhost:4444/api/user/firstpage
    @GetMapping("/page/{pageNum}")
    public Object getPage(
            RestTemplate restTemplate,
            @PathVariable("pageNum") String pageNumber

    ) {
        try {
            String url = "https://gorest.co.in/public/v2/users?page=" + pageNumber;

            ResponseEntity<UserModel[]> response = restTemplate.getForEntity(url, UserModel[].class);
            UserModel[] firstPageUsers = response.getBody();

            HttpHeaders responseHeaders = response.getHeaders();

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-pages")).get(0);

            System.out.println("Total pages: " + totalPages);

            for(int i = 0; i<firstPageUsers.length;i++){
                UserModel tempUser = firstPageUsers[i];
                System.out.println(tempUser.toString());
            }

            return new ResponseEntity<>(firstPageUsers,HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

            restTemplate.delete(url);//return void

            return "Successfully Deleted user #" + userId;


        } catch (HttpClientErrorException.NotFound exception) {
            return "User could not be delete, user #" + userId + "does not exist";
        } catch (HttpClientErrorException.Unauthorized exception) {
            return "You are not authorized to delete user #" + userId;

        } catch (Exception exception) {
            System.out.println(exception.getClass());
            return exception.getMessage();
        }
    }
//create
    @PostMapping("/qp")
    public Object postUserQueryParam(
           @RequestBody UserModel user,
            RestTemplate restTemplate
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/users/";
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

           // UserModel newUser = new UserModel(name, email, gender, status);

            //TODO: validate that gender and status are valid before continuing
            //TODO: validate that the email is a valid email

           // System.out.println("Date to be sent:\n" + newUser);

            HttpEntity<UserModel> request = new HttpEntity<>(user);

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

    @GetMapping("/all")
    public ResponseEntity getAllUser(RestTemplate restTemplate

    ) {

        try {
            ArrayList<UserModel> allusers = new ArrayList<>();
            String url = "https://gorest.co.in/public/v2/users";
            //instance of user model array
            ResponseEntity<UserModel[]> response = restTemplate.getForEntity(url, UserModel[].class);

            allusers.addAll(Arrays.asList(response.getBody()));

            int totalPageNumber = 4;// Integer.parseInt(response.getHeaders().get("X-Pagination-pages").get(0))
;
            //going through all the pages
            for(int i = 2;i <= totalPageNumber;i++){
                String tempUrl = url + "?page=" + i;
                UserModel[] pageData = restTemplate.getForObject(tempUrl,UserModel[].class);
                //allusers.addAll(Arrays.asList(pageData));
                allusers.addAll(Arrays.asList(Objects.requireNonNull(pageData)));
            }
            return new ResponseEntity<>(allusers,HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


        @PutMapping("/{id}")
        public ResponseEntity putUser(
                @PathVariable("id") String userId,
                RestTemplate restTemplate,
                @RequestBody UserModel updateData

        ){
        try{
            String url = "https://gorest.co.in/public/v2/users/" + userId;
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;
            HttpEntity<UserModel> request = new HttpEntity<>(updateData);
            ResponseEntity<UserModel> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    UserModel.class
            );
            return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
        }catch(HttpClientErrorException.UnprocessableEntity e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch(Exception e){
            System.out.println(e.getClass() + "\n" + e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        }



}


