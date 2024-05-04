package com.dev.restaurant.services.impl;

import com.dev.restaurant.DTO.AuthenticationRequest;
import com.dev.restaurant.DTO.StaffRequestDto;
import com.dev.restaurant.DTO.UserRequestDto;
import com.dev.restaurant.models.*;
import com.dev.restaurant.repositories.StaffRepository;
import com.dev.restaurant.repositories.TokenRepository;
import com.dev.restaurant.repositories.UserRepository;
import com.dev.restaurant.services.IAuthenticationService;
import com.dev.restaurant.services.IStaffService;
import com.dev.restaurant.services.IUserService;
import com.dev.restaurant.services.authenticationService.CustomUserDetailsService;
import com.dev.restaurant.util.FileHandler;
import com.dev.restaurant.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private final IUserService userService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final IStaffService staffService;
    @Autowired
    private final StaffRepository staffRepository;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final TokenRepository tokenRepository;

    private Token saveUserToken(User user) {

        final UserDetails userDetails =
                new CustomUserDetailsService(userService, staffService).loadUserByUsername(user.getEmail());
        Token t = new Token();
        t.setUser(userService.findUserByEmail(user.getEmail()).orElse(null));
        t.setToken(JwtTokenUtil.generateJwtToken(userDetails, 0));
        t.setTokenType(TokenType.BEARER);
        t.setRevoked(0);
        return tokenRepository.save(t);
    }

    private Token saveStaffToken(Staff staff) {
        final UserDetails userDetails =
                new CustomUserDetailsService(userService, staffService).loadUserByUsername(staff.getEmail());
        Token t = new Token();
        t.setStaff(staffService.findStaffByEmail(staff.getEmail()).orElse(null));
        t.setToken(JwtTokenUtil.generateJwtToken(userDetails, 0));
        t.setTokenType(TokenType.BEARER);
        t.setRevoked(0);
        return tokenRepository.save(t);
    }

    @Override
    public List<Object> createAuthenticationToken(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        Optional<User> user = userService.findUserByEmail(username);
        Optional<Staff> staff = staffService.findStaffByEmail(username);
        if (user.isEmpty() && staff.isEmpty())
            throw new BadCredentialsException("No account found matching username.");
        if (user.isPresent()) {
            if (user.get().getStatus() == Status.INACTIVE) {
                throw new BadCredentialsException("User is disabled.");
            }
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username,
                                password
                        )
                );
            } catch (BadCredentialsException exception) {
                throw new RuntimeException("Your password is incorrect. Please re-enter your password.");
            }
            User u = userRepository.save(user.get());
            userService.changeRevokeAllUserTokens(username, -1);
            String token = saveUserToken(u).getToken();
            return Arrays.asList(token,
                    user.get().getRole()
            );
        } else {
            if (staff.get().getStatus() == Status.INACTIVE) {
                throw new BadCredentialsException("Staff is disabled.");
            }
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username,
                                password
                        )
                );
            } catch (BadCredentialsException exception) {
                throw new RuntimeException("Your password is incorrect. Please re-enter your password.");
            }
            Staff s = staffRepository.save(staff.get());
            userService.changeRevokeAllUserTokens(username, -1);
            String token = saveStaffToken(s).getToken();
            return Arrays.asList(token,
                    staff.get().getRole()
            );
        }
    }


    @Override
    public UserRequestDto saveRegistration(User user, MultipartFile avatar) {
        try {
            user.setAvatar(FileHandler.saveFile(avatar));
        } catch (IOException e) {
            throw new RuntimeException("Error when saving avatar");
        }
        return userService.registerUser(user);

    }

    @Override
    public StaffRequestDto saveRegistration(Staff staff, MultipartFile avatar) {
        try {
            staff.setAvatar(FileHandler.saveFile(avatar));
        } catch (IOException e) {
            throw new RuntimeException("Error when saving avatar");
        }
        return staffService.registerStaff(staff);
    }

    @Override
    public List<?> getAccountInformationByToken(String token) {
        System.out.print(token);
        //List: revoked, isExpired, username, listRoles
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(tokenRepository);
        return tokenRepository.findByToken(token)
                .map(tokenRepo -> Arrays.asList(
                                tokenRepo.getRevoked(),
                                jwtTokenUtil.isTokenExpired(tokenRepo.getToken()),
                                jwtTokenUtil.extractUserName(tokenRepo.getToken()),
                                tokenRepository.findRoleByToken(tokenRepo.getToken())
                        )
                ).orElseThrow(() -> new RuntimeException("Your token is invalid, please login again to continue."));
    }


}
