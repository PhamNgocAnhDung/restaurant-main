package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.StaffRequestDto;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.models.Staff;
import com.dev.restaurant.services.IStaffService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class StaffController {
    private final IStaffService staffService;


    @GetMapping("/staff")
    public ResponseObject<Page<StaffRequestDto>> getStaff(@RequestParam(value = "_id", defaultValue = "-1") int id,
                                                          @RequestParam(value = "_email", defaultValue = "") String email,
                                                          @RequestParam(value = "_name", defaultValue = "") String name,
                                                          @RequestParam(value = "_phone_number", defaultValue = "") String phoneNumber,
                                                          @RequestParam(value = "_page", defaultValue = "0") int page,
                                                          @RequestParam(value = "_limit", defaultValue = "20") int limit,
                                                          @RequestParam(value = "_field", defaultValue = "id") String field,
                                                          @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
                                                          HttpServletRequest request
    ) {
        Page<StaffRequestDto> staffs = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            staffs = staffService.findStaffsWithPaginationAndSort(id, email, name, phoneNumber, page, limit, field, typeSort, request);
            if (staffs.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Staffs.",
                        staffs
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        staffs
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Staffs with filter. " + exception.getMessage(),
                    staffs
            );
        }
    }


    @GetMapping("/staff/information")
    public ResponseObject<StaffRequestDto> getStaffInformation(HttpServletRequest request) {

        try {
            Staff staff = staffService.getStaffInformation(request);
            if (staff != null)
                return new ResponseObject<>(
                        "ok",
                        "Get Staff Information success",
                        new StaffRequestDto(staff)
                );
            else
                return new ResponseObject<>(
                        "failed",
                        "Get Staff Information failed",
                        null
                );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Staffs with filter. " + exception.getMessage(),
                    null
            );
        }
    }

    @PostMapping("/staff")
    public ResponseObject<StaffRequestDto> addStaff(
            @RequestBody Staff staff
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Staff Successfully.",
                    staffService.saveStaff(staff)
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
    @PutMapping("/staff/{id}")
    public ResponseObject<StaffRequestDto> updateStaff(
            @PathVariable int id,
            @RequestBody Staff staff,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update staff successfully.",
                    staffService.updateStaff(id, staff, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update staff. " + exception.getMessage(),
                    null
            );
        }
    }


    @PutMapping("/staff/reset_password/{id}")
    public ResponseObject<StaffRequestDto> resetPassword(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Reset password to staff with id: " + id + " successfully.",
                    staffService.resetStaffPassword(id, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't reset password to Staff with id = " + id + ". " + exception.getMessage(),
                    null
            );
        }
    }


    //user
    @PutMapping("/staff/update_password")
    public ResponseObject<StaffRequestDto> updatePasswordToStaff(
            @RequestBody List<String> listPassword,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update staff password success",
                    staffService.updatePasswordToStaff(listPassword.get(0), listPassword.get(1), request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update staff password " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/staff/change_status/{id}")
    public ResponseObject<StaffRequestDto> changStatusStaff(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Change Status staff with id = " + id + " successfully.",
                    staffService.changeStatusStaff(id, request)
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
