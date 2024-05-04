package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.AuthenticationRequest;
import com.dev.restaurant.DTO.UserRequestDto;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.models.User;
import com.dev.restaurant.services.IAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@Controller
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
@CrossOrigin()
public class AuthController {

    @Autowired
    private final IAuthenticationService authenticationService;


    @PostMapping("/authenticate")
    public ResponseObject<List<Object>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Create Successfully.",
                    authenticationService.createAuthenticationToken(authenticationRequest)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    exception.getMessage(),
                    new ArrayList<>()
            );
        }
    }


    //////
    @PostMapping("/account_information")
    public ResponseObject<List<?>> getAccountInformationByToken(@RequestBody String token) {
        System.out.println(token);
        try {
            return new ResponseObject<>(
                    "ok",
                    "Get Account Information success.",
                    authenticationService.getAccountInformationByToken(token.replaceAll("\"", ""))
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    exception.getMessage(),
                    new ArrayList<>()
            );
        }
    }

    @PostMapping("")
    public ResponseObject<?> registration(@RequestParam("user") String user, @RequestParam("avatar") MultipartFile avatar) {
        System.out.println(user);
        try {
            if (avatar == null)
                throw new RuntimeException("User must have avatar");
            ObjectMapper objectMapper = new ObjectMapper();
            User userObject = objectMapper.readValue(user, User.class);

            return new ResponseObject<>(
                    "ok",
                    "Register Successfully.",
                    authenticationService.saveRegistration(userObject, avatar)
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseObject<>(
                    "error",
                    exception.getMessage(),
                    new UserRequestDto()
            );
        }
    }

}
