package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.FeedbackRequestDto;
import com.dev.restaurant.models.Feedback;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.services.IFeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class FeedbackController {
    private final IFeedbackService feedbackService;


    @GetMapping("/feedback")
    public ResponseObject<Page<FeedbackRequestDto>> getFeedback(
            @RequestParam(value = "_page", defaultValue = "0") int page,
            @RequestParam(value = "_limit", defaultValue = "20") int limit,
            @RequestParam(value = "_field", defaultValue = "id") String field,
            @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
            HttpServletRequest request
    ) {
        Page<FeedbackRequestDto> feedbacks = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            feedbacks = feedbackService.getFeedback(page, limit, field, typeSort, request);
            if (feedbacks.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Feedbacks.",
                        feedbacks
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        feedbacks
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Feedbacks with filter. " + exception.getMessage(),
                    feedbacks
            );
        }
    }


    @PostMapping("/feedback")
    public ResponseObject<FeedbackRequestDto> saveFeedback(
            @RequestBody Feedback feedback
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Feedback Successfully.",
                    feedbackService.saveFeedback(feedback)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Feedback. " + exception.getMessage(),
                    null
            );
        }
    }

}
