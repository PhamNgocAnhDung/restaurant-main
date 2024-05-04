package com.dev.restaurant.DTO;


import com.dev.restaurant.models.Feedback;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class FeedbackRequestDto {
    private int id;
    private String description;
    private LocalDateTime createdTime;


    public FeedbackRequestDto(Feedback feedback) {
        this.id = feedback.getId();
        this.description = feedback.getDescription();
        this.description = feedback.getDescription();
        this.createdTime = feedback.getCreatedTime();
    }

    public static Page<FeedbackRequestDto> fromFeedbacks(Page<Feedback> feedbacks, Pageable pageable) {
        return new PageImpl<>(
                feedbacks.getContent()
                        .stream()
                        .map(FeedbackRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                feedbacks.getTotalElements()
        );
    }

    public static List<FeedbackRequestDto> fromFeedbacks(List<Feedback> feedbacks) {
        return
                feedbacks.stream()
                        .map(FeedbackRequestDto::new)
                        .collect(Collectors.toList()
                        );

    }
}
