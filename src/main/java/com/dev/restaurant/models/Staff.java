package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "staffs")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(nullable = false, length = 40)
    private String firstName;
    @NotBlank
    @Column(nullable = false, length = 10)
    private String lastName;
    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @NotBlank
    @Column(nullable = false)
    @Builder.Default
    private String password = new BCryptPasswordEncoder().encode("Abcd@1234");
    @NotBlank
    @Column(nullable = false)
    private String address;
    @NotBlank
    @Column(nullable = false)
    private String phoneNumber;
    @NotBlank
    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    @NotBlank
    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF;
    @NotBlank
    private String avatar;
    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    @JsonBackReference("staff-tokens")
    private List<Token> tokens;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "BRAND_ID", referencedColumnName = "id")
    @JsonIgnoreProperties("staffs-brand")
    private Brand brand;
    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    @JsonBackReference("staff-assignments")
    private List<Assignment> assignments=new ArrayList<>();
}