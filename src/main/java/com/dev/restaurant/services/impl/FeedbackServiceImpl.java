package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.FeedbackRequestDto;
import com.dev.restaurant.models.Feedback;
import com.dev.restaurant.repositories.FeedbackRepository;
import com.dev.restaurant.services.IFeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {
    @Autowired
    private final FeedbackRepository feedbackRepository;


    @Override
    public Page<FeedbackRequestDto> getFeedback(int page, int limit, String field, String typeSort, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));

        return FeedbackRequestDto.fromFeedbacks(
                feedbackRepository.findAll(pageable), pageable
        );
    }

    @Override
    public FeedbackRequestDto saveFeedback(Feedback feedback) {
        return new FeedbackRequestDto(feedbackRepository.save(feedback));

    }
}
