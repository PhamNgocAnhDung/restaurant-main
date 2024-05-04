package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.UserRequestDto;
import com.dev.restaurant.models.Role;
import com.dev.restaurant.models.Staff;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.User;
import com.dev.restaurant.repositories.StaffRepository;
import com.dev.restaurant.repositories.TokenRepository;
import com.dev.restaurant.repositories.UserRepository;
import com.dev.restaurant.services.IUserService;
import com.dev.restaurant.util.FileHandler;
import com.dev.restaurant.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final StaffRepository staffRepository;

    @Override
    public User getUserInformation(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            if (!request.getHeader("Authorization").startsWith("Bearer "))
                throw new RuntimeException("Token is invalid.");
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(tokenRepository);
            String token = request.getHeader("Authorization").substring(7);
            return userRepository.findByEmail(jwtTokenUtil.extractUserName(token)).map(user -> {
                if (jwtTokenUtil.isTokenExpired(token))
                    throw new RuntimeException("Your token has expired, please re-enter to make a new token.");
                if (user.getStatus() == Status.INACTIVE)
                    throw new RuntimeException(
                            "The user of the above token has been disabled.");
                return user;
            }).orElse(null);
        }
        return null;
    }

    @Override
    public UserRequestDto registerUser(User user) {
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException(
                    "Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercase letter, one number and one special character in the following @#$%^&+=_!");

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
       
        Staff staffFind = staffRepository.findByEmail(user.getEmail()).orElse(null);
        User userFind = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (staffFind != null || userFind != null)
            throw new RuntimeException("Email already exists, please enter another email to continue.");
        try {
            User savedUser = userRepository.save(user);
            return new UserRequestDto(savedUser);
        } catch (Exception exception) {
            throw new RuntimeException("Error when query");
        }
    }

    @Override
    public UserRequestDto saveUser(User user, MultipartFile avatar) {
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException(
                    "Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercase letter, one number and one special character in the following @#$%^&+=_!");
        Staff staffFind = staffRepository.findByEmail(user.getEmail()).orElse(null);
        User userFind = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (staffFind != null || userFind != null)
            throw new RuntimeException("Email already exists, please enter another email to continue.");
        try {
            User u = new User();
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEmail(user.getEmail());
            u.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            u.setAddress(user.getAddress());
            u.setPhoneNumber(user.getPhoneNumber());
            u.setStatus(Status.ACTIVE);
            u.setRole(user.getRole());
            u.setAvatar(FileHandler.saveFile(avatar));
            User savedUser = userRepository.save(u);
            return new UserRequestDto(savedUser);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error when query");
        }
    }

    @Override
    public UserRequestDto updateUser(int id, User user, MultipartFile avatar, HttpServletRequest request) {
        if (Objects.equals(id, 0))
            throw new RuntimeException("You cannot edit the system default account.");
        else
            return userRepository.findById(id).map(updateUser -> {
                boolean checkChangeEmail = false;
                updateUser.setRole(user.getRole());
                if (!Objects.equals(updateUser.getEmail(), user.getEmail())
                        && (userRepository.findByEmail(user.getEmail()).isPresent() || staffRepository.findByEmail(user.getEmail()).isPresent()))
                    throw new RuntimeException("Email already exists, please enter another email to continue.");
                if (!Objects.equals(updateUser.getEmail(), user.getEmail())) {
                    updateUser.setEmail(user.getEmail());
                    checkChangeEmail = true;
                }
                updateUser.setFirstName(user.getFirstName());
                updateUser.setLastName(user.getLastName());
                updateUser.setAddress(user.getAddress());
                updateUser.setPhoneNumber(user.getPhoneNumber());
                if (avatar != null) {
                    try {
                        updateUser.setAvatar(FileHandler.saveFile(avatar));
                    } catch (IOException e) {
                        throw new RuntimeException("Failed save avatar image");
                    }
                }
                UserRequestDto result = new UserRequestDto(userRepository.save(updateUser));
                if (checkChangeEmail)
                    changeRevokeAllUserTokens(updateUser.getEmail(), -1);
                return result;
            }).orElseThrow(() -> new RuntimeException("User not found."));
    }


    @Override
    public UserRequestDto changeStatusUser(int userId, HttpServletRequest request) {
        if (Objects.equals(userId, 0))
            throw new RuntimeException("You cannot edit the system default account.");
        else
            return userRepository.findById(userId).map(user -> {
                user.setStatus(user.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
                changeRevokeAllUserTokens(user.getEmail(), user.getStatus() == Status.ACTIVE ? 0 : user.getStatus() == Status.INACTIVE ? 1 : -1);
                return new UserRequestDto(userRepository.save(user));
            }).orElseThrow(() -> new RuntimeException("User not found."));

    }

    @Override
    public Page<UserRequestDto> findUsersWithPaginationAndSort(
            int id,
            String email,
            String name,
            String phoneNumber,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
        return UserRequestDto.fromUsers(userRepository.findUsersWithPaginationAndSort(id, email, name,
                phoneNumber, pageable), pageable);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error when querying.");
        }
    }

    @Override
    public void changeRevokeAllUserTokens(String username, int value) {
        try {
            tokenRepository.changeRevokeWithEmailUser(username, value);
        } catch (Exception exception) {
            throw new RuntimeException("Error when querying.");
        }
    }


    @Override
    public UserRequestDto resetUserPassword(int id, HttpServletRequest request) {
        try {

            return userRepository.findById(id).map(u -> {
                u.setPassword(new BCryptPasswordEncoder().encode("Abcd@1234"));
                return new UserRequestDto(userRepository.save(u));
            }).orElseThrow(() -> new RuntimeException("User not found"));

        } catch (Exception e) {
            throw new RuntimeException("Error when querying.");
        }
    }

    @Override
    public UserRequestDto updatePasswordToUser(String oldPassword, String newPassword, HttpServletRequest request) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(tokenRepository);
        User user = userRepository.findByEmail(jwtTokenUtil.extractUserName(request.getHeader("Authorization").substring(7))).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException("Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercases letter, one number and one special character in the following @#$%^&+=_!");
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        return new UserRequestDto(userRepository.save(user));
    }


}
