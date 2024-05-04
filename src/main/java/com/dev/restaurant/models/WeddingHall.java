package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "wedding_halls")
public class WeddingHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NotBlank
    private String weddingHallName;

    @Column(nullable = false)
    @NotBlank
    private String weddingHallLocation;

    @Column(nullable = false)
    @NotBlank
    private Long morningPrice;

    @Column(nullable = false)
    @NotBlank
    private Long noonPrice;

    @Column(nullable = false)
    @NotBlank
    private Long eveningPrice;

    @Column(nullable = false)
    @NotBlank
    private Long weekendPrice;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now().withNano(0);

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "BRAND_ID", referencedColumnName = "id")
    @JsonIgnoreProperties("wedding-halls-brand")
    private Brand brand;

}
