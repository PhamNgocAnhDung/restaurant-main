package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.AssignmentRequestDto;
import com.dev.restaurant.models.Assignment;
import com.dev.restaurant.models.AssignmentStatus;
import com.dev.restaurant.models.WeddingPartyInformation;
import com.dev.restaurant.repositories.AssignmentRepository;
import com.dev.restaurant.repositories.StaffRepository;
import com.dev.restaurant.repositories.WeddingPartyInformationRepository;
import com.dev.restaurant.services.IAssignmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentServiceImpl implements IAssignmentService {
    @Autowired
    private final AssignmentRepository assignmentRepository;
    @Autowired
    private final WeddingPartyInformationRepository weddingPartyInformationRepository;
    @Autowired
    private final StaffRepository staffRepository;

    @Override
    public Page<AssignmentRequestDto> getAssignmentByWeddingPartyId(int weddingPartyId, int page, int limit, String field, String typeSort, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
        return AssignmentRequestDto.fromAssignments(assignmentRepository.findByWeddingPartyId(weddingPartyId, pageable), pageable);
    }

    @Override
    public AssignmentRequestDto saveAssignment(Assignment assignment) {
        return new AssignmentRequestDto(assignmentRepository.save(assignment));
    }

    @Override
    public List<AssignmentRequestDto> saveAssignments(int weddingPartyId, List<Map<String, Object>> listAssignment) {
        WeddingPartyInformation weddingPartyInformation = weddingPartyInformationRepository.findById(weddingPartyId).orElseThrow(() -> new RuntimeException("Wedding Party not found"));

        return AssignmentRequestDto.fromAssignments(
                assignmentRepository.saveAll(
                        listAssignment.stream()
                                .map(a -> Assignment.builder()
                                        .weddingPartyInformation(weddingPartyInformation)
                                        .staff(staffRepository.findById((int) a.get("staffId")).orElseThrow(() -> new RuntimeException("User " + a.get("staffId") + " not found")))
                                        .note(a.get("note").toString())
                                        .build()
                                ).toList()
                )
        );
    }

    @Override
    public AssignmentRequestDto updateAssignment(Assignment assignment) {
        return new AssignmentRequestDto(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentRequestDto completeAssignment(Long assignmentId) {
        return assignmentRepository.findById(assignmentId).map(a -> {
            a.setAssignmentStatus(AssignmentStatus.COMPLETED);
            return new AssignmentRequestDto(assignmentRepository.save(a));
        }).orElseThrow(() -> new RuntimeException("Assignment not found"));

    }
}
