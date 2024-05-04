package com.dev.restaurant.services;


import com.dev.restaurant.DTO.AssignmentRequestDto;
import com.dev.restaurant.models.Assignment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IAssignmentService {


    Page<AssignmentRequestDto> getAssignmentByWeddingPartyId(int weddingPartyId,
                                                             int page,
                                                             int limit,
                                                             String field,
                                                             String typeSort,
                                                             HttpServletRequest request);

    AssignmentRequestDto saveAssignment(Assignment assignment);

    List<AssignmentRequestDto> saveAssignments(int weddingPartyId, List<Map<String, Object>> listAssignment);

    AssignmentRequestDto updateAssignment(Assignment assignment);

    AssignmentRequestDto completeAssignment(Long assignmentId);
}