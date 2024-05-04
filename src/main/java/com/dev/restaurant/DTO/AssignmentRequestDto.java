package com.dev.restaurant.DTO;


import com.dev.restaurant.models.Assignment;
import com.dev.restaurant.models.AssignmentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AssignmentRequestDto {
    private Long id;
    private String note;
    private AssignmentStatus assignmentStatus;


    public AssignmentRequestDto(Assignment assignment) {
        this.id = assignment.getId();
        this.note = assignment.getNote();
        this.assignmentStatus = assignment.getAssignmentStatus();

    }

    public static Page<AssignmentRequestDto> fromAssignments(Page<Assignment> assignments, Pageable pageable) {
        return new PageImpl<>(
                assignments.getContent()
                        .stream()
                        .map(AssignmentRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                assignments.getTotalElements()
        );
    }

    public static List<AssignmentRequestDto> fromAssignments(List<Assignment> assignments) {
        return
                assignments.stream()
                        .map(AssignmentRequestDto::new)
                        .collect(Collectors.toList()
                        );

    }
}
