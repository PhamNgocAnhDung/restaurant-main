package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
@Table(name = "wedding_services")
public class WeddingService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NotBlank
    private String weddingServiceName;

    @Column(nullable = false)
    @NotBlank
    private Long weddingServicePrice;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now().withNano(0);

    @JsonIgnore
    @JsonIgnoreProperties("wedding-services-brands")
    @ManyToMany
    @JoinTable(
            name = "brands_wedding_services",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "wedding_service_id"))
    @Builder.Default
    private List<Brand> brands = new ArrayList<>();

    @JsonIgnore
    @JsonIgnoreProperties("wedding-services-wedding_party_informations")
    @ManyToMany
    @JoinTable(
            name = "wedding_party_informations_wedding_services",
            joinColumns = @JoinColumn(name = "wedding_party_information_id"),
            inverseJoinColumns = @JoinColumn(name = "wedding_service_id"))
    @Builder.Default
    private List<WeddingPartyInformation> weddingPartyInformations = new ArrayList<>();
}
