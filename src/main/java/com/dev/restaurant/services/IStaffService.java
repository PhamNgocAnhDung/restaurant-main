package com.dev.restaurant.services;


import com.dev.restaurant.DTO.StaffRequestDto;
import com.dev.restaurant.models.Staff;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IStaffService {


    Staff getStaffInformation(HttpServletRequest request);

    StaffRequestDto saveStaff(Staff staff);

    StaffRequestDto registerStaff(Staff staff);

    StaffRequestDto updateStaff(int id, Staff staff, HttpServletRequest request);


    StaffRequestDto updatePasswordToStaff(String oldPassword, String newPassword, HttpServletRequest request);

    StaffRequestDto changeStatusStaff(int staffId, HttpServletRequest request);

    Page<StaffRequestDto> findStaffsWithPaginationAndSort(
            int id,
            String email,
            String name,
            String phoneNumber,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);

    Optional<Staff> findStaffByEmail(String email);

    void changeRevokeAllStaffTokens(String username, int value);

    StaffRequestDto resetStaffPassword(int id, HttpServletRequest request);

    void staffInitialization();
}