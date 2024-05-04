package com.dev.restaurant.services;


import com.dev.restaurant.DTO.FeedbackRequestDto;
import com.dev.restaurant.models.Feedback;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface IFeedbackService {


    Page<FeedbackRequestDto> getFeedback(int page,
                                         int limit,
                                         String field,
                                         String typeSort,
                                         HttpServletRequest request);

    FeedbackRequestDto saveFeedback(Feedback feedback);
}