package com.dev.restaurant.DTO;



import com.dev.restaurant.models.Role;
import com.dev.restaurant.models.Staff;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StaffRequestDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private Role role;
        private String avatar;
    private Status status;



    public StaffRequestDto(Staff staff) {
        this.id = staff.getId();
        this.firstName = staff.getFirstName();
        this.lastName = staff.getLastName();
        this.email = staff.getEmail();
        this.address = staff.getAddress();
        this.phoneNumber = staff.getPhoneNumber();
        this.status = staff.getStatus();
        this.role = staff.getRole();
        this.avatar = staff.getAvatar();
    }

    public static Page<StaffRequestDto> fromStaffs(Page<Staff> staffs, Pageable pageable) {
        return new PageImpl<>(
                staffs.getContent()
                        .stream()
                        .map(StaffRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                staffs.getTotalElements()
        );
    }
}
