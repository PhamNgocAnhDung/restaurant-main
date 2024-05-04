package com.dev.restaurant.DTO;



import com.dev.restaurant.models.Role;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private Role role;
    private String avatar;
    private Status status;



    public UserRequestDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.status = user.getStatus();
        this.role = user.getRole();
        this.avatar = user.getAvatar();
    }

    public static Page<UserRequestDto> fromUsers(Page<User> users, Pageable pageable) {
        return new PageImpl<>(
                users.getContent()
                        .stream()
                        .map(UserRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                users.getTotalElements()
        );
    }
}
