package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private int revoked;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
//    @JsonManagedReference
    private User user;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "id")
//    @JsonManagedReference
    private Staff staff;

}
