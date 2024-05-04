package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.AssignmentRequestDto;
import com.dev.restaurant.models.Assignment;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.services.IAssignmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class AssignmentController {
    private final IAssignmentService assignmentService;


    @GetMapping("/assignment/{weddingPartyId}")
    public ResponseObject<Page<AssignmentRequestDto>> get(@PathVariable int weddingPartyId,
                                                          @RequestParam(value = "_page", defaultValue = "0") int page,
                                                          @RequestParam(value = "_limit", defaultValue = "20") int limit,
                                                          @RequestParam(value = "_field", defaultValue = "id") String field,
                                                          @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
                                                          HttpServletRequest request
    ) {
        Page<AssignmentRequestDto> staffs = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            staffs = assignmentService.getAssignmentByWeddingPartyId(weddingPartyId, page, limit, field, typeSort, request);
            if (staffs.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Assignments.",
                        staffs
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        staffs
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Assignments with filter. " + exception.getMessage(),
                    staffs
            );
        }
    }


    @PostMapping("/assignment")
    public ResponseObject<AssignmentRequestDto> saveAssignment(
            @RequestBody Assignment assignment
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Assignment Successfully.",
                    assignmentService.saveAssignment(assignment)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Assignment. " + exception.getMessage(),
                    null
            );
        }
    }

    @PostMapping("/assignment/{weddingPartyId}")
    public ResponseObject<List<AssignmentRequestDto>> saveAssignments(
            @PathVariable int weddingPartyId,
            @RequestBody List<Map<String, Object>> assignment
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Assignments Successfully.",
                    assignmentService.saveAssignments(weddingPartyId, assignment)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Assignments. " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/assignment")
    public ResponseObject<AssignmentRequestDto> updateAssignment(
            @RequestBody Assignment assignment
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update Assignment Successfully.",
                    assignmentService.updateAssignment(assignment)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update Assignment. " + exception.getMessage(),
                    null
            );
        }
    }

    @PostMapping("/assignment/complete/{id}")
    public ResponseObject<AssignmentRequestDto> saveAssignments(
            @PathVariable Long id
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Assignments Successfully.",
                    assignmentService.completeAssignment(id)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Assignments. " + exception.getMessage(),
                    null
            );
        }
    }

}
