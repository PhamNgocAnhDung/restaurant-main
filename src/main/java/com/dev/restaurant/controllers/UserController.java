package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.UserRequestDto;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.models.User;
import com.dev.restaurant.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class UserController {
    private final IUserService userService;


    @GetMapping("/user")
    public ResponseObject<Page<UserRequestDto>> getUser(@RequestParam(value = "_id", defaultValue = "-1") int id,
                                                        @RequestParam(value = "_email", defaultValue = "") String email,
                                                        @RequestParam(value = "_name", defaultValue = "") String name,
                                                        @RequestParam(value = "_phone_number", defaultValue = "") String phoneNumber,
                                                        @RequestParam(value = "_page", defaultValue = "0") int page,
                                                        @RequestParam(value = "_limit", defaultValue = "20") int limit,
                                                        @RequestParam(value = "_field", defaultValue = "id") String field,
                                                        @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
                                                        HttpServletRequest request
    ) {
        Page<UserRequestDto> users = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            users = userService.findUsersWithPaginationAndSort(id, email, name, phoneNumber, page, limit, field, typeSort, request);
            if (users.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Users.",
                        users
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        users
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Users with filter. " + exception.getMessage(),
                    users
            );
        }
    }


    @GetMapping("/user/information")
    public ResponseObject<UserRequestDto> getUserInformation(HttpServletRequest request) {

        try {
            User user = userService.getUserInformation(request);
            if (user != null)
                return new ResponseObject<>(
                        "ok",
                        "Get User Information success",
                        new UserRequestDto(user)
                );
            else
                return new ResponseObject<>(
                        "failed",
                        "Get User Information failed",
                        null
                );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Users with filter. " + exception.getMessage(),
                    null
            );
        }
    }

    @PostMapping("/user")
    public ResponseObject<UserRequestDto> addUser(
            @RequestParam("user") String user,
            @RequestParam("avatar") MultipartFile avatar
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add User Successfully.",
                    userService.saveUser(new ObjectMapper().readValue(user, User.class), avatar)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add user. " + exception.getMessage(),
                    null
            );
        }
    }

    //admin
    @PutMapping("/user/{id}")
    public ResponseObject<UserRequestDto> updateUser(
            @PathVariable int id,
            @RequestParam("user") String user,
            @RequestParam("avatar") MultipartFile avatar,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update user successfully.",
                    userService.updateUser(id, new ObjectMapper().readValue(user, User.class), avatar, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update user. " + exception.getMessage(),
                    null
            );
        }
    }


    @PutMapping("/user/reset_password/{id}")
    public ResponseObject<UserRequestDto> resetPassword(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Reset password to user with id: " + id + " successfully.",
                    userService.resetUserPassword(id, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't reset password to user with id = " + id + ". " + exception.getMessage(),
                    null
            );
        }
    }


    //user
    @PutMapping("/user/update_password")
    public ResponseObject<UserRequestDto> updatePasswordToUser(
            @RequestBody List<String> listPassword,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update user password success",
                    userService.updatePasswordToUser(listPassword.get(0), listPassword.get(1), request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update user password " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/user/change_status/{id}")
    public ResponseObject<UserRequestDto> changStatusUser(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Change Status user with id = " + id + " successfully.",
                    userService.changeStatusUser(id, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Cannot change status User with id = " + id + ". " + exception.getMessage(),
                    null
            );
        }
    }

}
