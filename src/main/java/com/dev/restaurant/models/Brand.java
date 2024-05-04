package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String brandName;
    @Column(nullable = false)
    @NotBlank
    private String brandAddress;
    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now().withNano(0);

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    @JsonIgnoreProperties("brand-staffs")
    private List<Staff> staffs=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    @JsonIgnoreProperties("brand-wedding-halls")
    @Builder.Default
    private List<WeddingHall> weddingHalls=new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "brands")
    @JsonIgnoreProperties("brands-wedding-services")
    @Builder.Default
    private List<WeddingService> weddingServices=new ArrayList<>();
}
