package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.StaffRequestDto;
import com.dev.restaurant.models.Role;
import com.dev.restaurant.models.Staff;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.User;
import com.dev.restaurant.repositories.StaffRepository;
import com.dev.restaurant.repositories.TokenRepository;
import com.dev.restaurant.repositories.UserRepository;
import com.dev.restaurant.services.IStaffService;
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

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffServiceImpl implements IStaffService {
    @Autowired
    private final StaffRepository staffRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TokenRepository tokenRepository;

    @Override
    public Staff getStaffInformation(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            if (!request.getHeader("Authorization").startsWith("Bearer "))
                throw new RuntimeException("Token is invalid.");
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(tokenRepository);
            String token = request.getHeader("Authorization").substring(7);
            return staffRepository.findByEmail(jwtTokenUtil.extractUserName(token)).map(staff -> {
                if (jwtTokenUtil.isTokenExpired(token))
                    throw new RuntimeException("Your token has expired, please re-enter to make a new token.");
                if (staff.getStatus() == Status.INACTIVE)
                    throw new RuntimeException(
                            "The staff of the above token has been disabled.");
                return staff;
            }).orElse(null);
        }
        return null;
    }


    @Override
    public StaffRequestDto saveStaff(Staff staff) {
        if (!staff.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException(
                    "Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercase letter, one number and one special character in the following @#$%^&+=_!");
        Staff staffFind = staffRepository.findByEmail(staff.getEmail()).orElse(null);
        User userFind = userRepository.findByEmail(staff.getEmail()).orElse(null);
        if (staffFind != null || userFind != null)
            throw new RuntimeException("Email already exists, please enter another email to continue.");
        try {
            Staff u = new Staff();
            u.setFirstName(staff.getFirstName());
            u.setLastName(staff.getLastName());
            u.setEmail(staff.getEmail());
            u.setPassword(new BCryptPasswordEncoder().encode(staff.getPassword()));
            u.setAddress(staff.getAddress());
            u.setPhoneNumber(staff.getPhoneNumber());
            u.setStatus(Status.ACTIVE);
            u.setRole(staff.getRole());
            Staff savedStaff = staffRepository.save(u);
            return new StaffRequestDto(savedStaff);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error when query");
        }
    }

    @Override
    public StaffRequestDto registerStaff(Staff staff) {
        if (!staff.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException(
                    "Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercase letter, one number and one special character in the following @#$%^&+=_!");

        staff.setPassword(new BCryptPasswordEncoder().encode(staff.getPassword()));
        staff.setStatus(Status.ACTIVE);
        staff.setRole(Role.USER);
        Staff staffFind = staffRepository.findByEmail(staff.getEmail()).orElse(null);
        User userFind = userRepository.findByEmail(staff.getEmail()).orElse(null);
        if (staffFind != null || userFind != null)
            throw new RuntimeException("Email already exists, please enter another email to continue.");
        try {
            Staff savedStaff = staffRepository.save(staff);
            return new StaffRequestDto(savedStaff);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error when query");
        }
    }

    @Override
    public StaffRequestDto updateStaff(int id, Staff staff, HttpServletRequest request) {
        if (Objects.equals(id, 0))
            throw new RuntimeException("You cannot edit the system default account.");
        else
            return staffRepository.findById(id).map(updateStaff -> {
                boolean checkChangeEmail = false;

                updateStaff.setRole(staff.getRole());
                if (!Objects.equals(updateStaff.getEmail(), staff.getEmail())
                        && (staffRepository.findByEmail(staff.getEmail()).isPresent() || userRepository.findByEmail(staff.getEmail()).isPresent()))
                    throw new RuntimeException("Email already exists, please enter another email to continue.");
                if (!Objects.equals(updateStaff.getEmail(), staff.getEmail())) {
                    updateStaff.setEmail(staff.getEmail());
                    checkChangeEmail = true;
                }
                updateStaff.setFirstName(staff.getFirstName());
                updateStaff.setLastName(staff.getLastName());
                updateStaff.setAddress(staff.getAddress());
                updateStaff.setPhoneNumber(staff.getPhoneNumber());
                updateStaff.setAvatar(staff.getAvatar());
                StaffRequestDto result = new StaffRequestDto(staffRepository.save(updateStaff));
                if (checkChangeEmail)
                    changeRevokeAllStaffTokens(updateStaff.getEmail(), -1);
                return result;
            }).orElseThrow(() -> new RuntimeException("User not found."));

    }


    @Override
    public StaffRequestDto updatePasswordToStaff(String oldPassword, String newPassword, HttpServletRequest request) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(tokenRepository);
        Staff staff = staffRepository.findByEmail(jwtTokenUtil.extractUserName(request.getHeader("Authorization").substring(7))).orElse(null);
        if (staff == null) {
            throw new RuntimeException("Staff not found");
        }
        if (!new BCryptPasswordEncoder().matches(oldPassword, staff.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException("Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercases letter, one number and one special character in the following @#$%^&+=_!");
        staff.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        return new StaffRequestDto(staffRepository.save(staff));
    }


    @Override
    public StaffRequestDto changeStatusStaff(int staffId, HttpServletRequest request) {
        if (Objects.equals(staffId, 0))
            throw new RuntimeException("You cannot edit the system default account.");
        else
            return staffRepository.findById(staffId).map(staff -> {
                staff.setStatus(staff.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
                changeRevokeAllStaffTokens(staff.getEmail(), staff.getStatus() == Status.ACTIVE ? 0 : staff.getStatus() == Status.INACTIVE ? 1 : -1);
                return new StaffRequestDto(staffRepository.save(staff));
            }).orElseThrow(() -> new RuntimeException("Staff not found."));

    }

    @Override
    public Page<StaffRequestDto> findStaffsWithPaginationAndSort(
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
        return StaffRequestDto.fromStaffs(staffRepository.findStaffsWithPaginationAndSort(id, email, name,
                phoneNumber, pageable), pageable);
    }

    @Override
    public Optional<Staff> findStaffByEmail(String email) {
        try {
            return staffRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error when querying.");
        }
    }

    @Override
    public void changeRevokeAllStaffTokens(String username, int value) {
        try {
            tokenRepository.changeRevokeWithEmailStaff(username, value);
        } catch (Exception exception) {
            throw new RuntimeException("Error when querying.");
        }
    }


    @Override
    public StaffRequestDto resetStaffPassword(int id, HttpServletRequest request) {
        try {
            return staffRepository.findById(id).map(u -> {
                u.setPassword(new BCryptPasswordEncoder().encode("Abcd@1234"));
                return new StaffRequestDto(staffRepository.save(u));
            }).orElseThrow(() -> new RuntimeException("Staff not found"));

        } catch (Exception e) {
            throw new RuntimeException("Error when querying.");
        }
    }

    @Override
    public void staffInitialization() {
        if (staffRepository.findByEmail("admin@gmail.com").isEmpty()) {
            try {
                Staff u = new Staff();
                u.setFirstName("Admin");
                u.setLastName("Admin");
                u.setEmail("admin@gmail.com");
                u.setPassword(new BCryptPasswordEncoder().encode("admin_Abcd@1234"));
                u.setAddress("17A Cộng Hòa");
                u.setPhoneNumber("0373926165");
                u.setStatus(Status.ACTIVE);
                u.setRole(Role.ADMIN);
                staffRepository.save(u);
            } catch (Exception e) {

                throw new RuntimeException("Error when querying.");
            }
        }
    }
}
