package com.dev.restaurant.services;

import com.dev.restaurant.DTO.AuthenticationRequest;
import com.dev.restaurant.DTO.StaffRequestDto;
import com.dev.restaurant.DTO.UserRequestDto;
import com.dev.restaurant.models.Staff;
import com.dev.restaurant.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IAuthenticationService {


    List<Object> createAuthenticationToken(AuthenticationRequest authenticationRequest);

    UserRequestDto saveRegistration(User user, MultipartFile avatar);

    StaffRequestDto saveRegistration(Staff staff, MultipartFile avatar);

    List<?> getAccountInformationByToken(String token);


}
