package com.dev.restaurant.DTO;


import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.WeddingService;
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
public class WeddingServiceRequestDto {
    private int id;
    private String weddingServiceName;
    private Long weddingServicePrice;
    private Status status;
    private LocalDateTime createdTime;


    public WeddingServiceRequestDto(WeddingService weddingService) {
        this.id = weddingService.getId();
        this.weddingServiceName = weddingService.getWeddingServiceName();
        this.weddingServicePrice = weddingService.getWeddingServicePrice();
        this.status = weddingService.getStatus();
        this.createdTime = weddingService.getCreatedTime();
    }

    public static Page<WeddingServiceRequestDto> fromWeddingServices(Page<WeddingService> weddingServices, Pageable pageable) {
        return new PageImpl<>(
                weddingServices.getContent()
                        .stream()
                        .map(WeddingServiceRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                weddingServices.getTotalElements()
        );
    }

    public static List<WeddingServiceRequestDto> fromWeddingServices(List<WeddingService> weddingServices) {
        return
                weddingServices.stream()
                        .map(WeddingServiceRequestDto::new)
                        .collect(Collectors.toList()
                        );

    }
}
