package com.dev.restaurant.services;


import com.dev.restaurant.DTO.UserRequestDto;
import com.dev.restaurant.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public interface IUserService {

    UserRequestDto changeStatusUser(int userId, HttpServletRequest request);


    UserRequestDto updateUser(int id, User user, MultipartFile avatar, HttpServletRequest request);


    Page<UserRequestDto> findUsersWithPaginationAndSort(
            int id,
            String email,
            String name,
            String phoneNumber,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);

    Optional<User> findUserByEmail(String email);


    User getUserInformation(HttpServletRequest request);

    UserRequestDto registerUser(User user);

    UserRequestDto saveUser(User user, MultipartFile avatar);


    UserRequestDto updatePasswordToUser(String oldPassword, String newPassword, HttpServletRequest request);


    ////////////////////////////////Important
    void changeRevokeAllUserTokens(String username, int value);

    UserRequestDto resetUserPassword(int id, HttpServletRequest request);


}