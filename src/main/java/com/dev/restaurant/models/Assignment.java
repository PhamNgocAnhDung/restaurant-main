package com.dev.restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "WEDDING_PARTY_ID", referencedColumnName = "id")
    @JsonIgnoreProperties("assignments-wedding-party")
    private WeddingPartyInformation weddingPartyInformation;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "id")
    @JsonIgnoreProperties("assignments-staff")
    private Staff staff;

    @Column
    private String note;

    @Column
    @NotNull
    @Builder.Default
    private AssignmentStatus assignmentStatus = AssignmentStatus.INCOMPLETE;
}
