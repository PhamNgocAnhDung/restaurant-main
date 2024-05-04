package com.dev.restaurant.services.authenticationService;


import com.dev.restaurant.models.Role;
import com.dev.restaurant.models.Staff;
import com.dev.restaurant.models.User;
import com.dev.restaurant.services.IStaffService;
import com.dev.restaurant.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final IUserService userService;
    @Autowired
    private final IStaffService staffService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(email).orElse(null);
        Staff staff = staffService.findStaffByEmail(email).orElse(null);
        if (user != null || staff!=null) {
            return new org.springframework.security.core.userdetails.User(
                    user != null ?user.getEmail():staff.getEmail(),
                    user != null ?user.getPassword():staff.getPassword() ,
                      mapRolesToAuthorities(user != null ?user.getRole():staff.getRole()));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return Stream.of(role)
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toList());
    }
}
