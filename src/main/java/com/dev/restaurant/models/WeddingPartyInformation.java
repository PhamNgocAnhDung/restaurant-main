package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "wedding_party_informations")
public class WeddingPartyInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NotBlank
    @Builder.Default
    private LocalDateTime partyBookingDate = LocalDateTime.now().withNano(0);

    @Column(nullable = false)
    @NotBlank
    private LocalDateTime partyDate;
    @Column(nullable = false)
    @NotBlank
    private Long totalPrice;

    @Column(nullable = false)
    @NotBlank
    @JsonIgnore
    private String weddingServiceIds;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "PARTY_HOST_ID", referencedColumnName = "id")
    @JsonIgnoreProperties("wedding-party-user")
    private User partyHost;


    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "WEDDING_HALL_ID", referencedColumnName = "id")
    @JsonIgnoreProperties("wedding-party-user")
    private WeddingHall weddingHall;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WeddingHallType weddingHallType;

    @JsonIgnore
    @ManyToMany(mappedBy = "weddingPartyInformations")
    @JsonIgnoreProperties("wedding-party-informations-wedding-services")
    @Builder.Default
    private List<WeddingService> weddingServices = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "weddingPartyInformation")
    @JsonBackReference("wedding-party-assignments")
    private List<Assignment> assignments = new ArrayList<>();
}
