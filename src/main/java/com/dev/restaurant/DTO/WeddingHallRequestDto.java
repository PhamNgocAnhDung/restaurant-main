package com.dev.restaurant.DTO;


import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.WeddingHall;
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
public class WeddingHallRequestDto {
    private int id;
    private String weddingHallName;
    private String weddingHallLocation;
    private Long morningPrice;
    private Long noonPrice;
    private Long eveningPrice;
    private Long weekendPrice;
    private Status status;
    private LocalDateTime createdTime;


    public WeddingHallRequestDto(WeddingHall weddingHall) {
        this.id = weddingHall.getId();
        this.weddingHallLocation = weddingHall.getWeddingHallLocation();
        this.weddingHallName = weddingHall.getWeddingHallName();
        this.morningPrice = weddingHall.getMorningPrice();
        this.noonPrice = weddingHall.getNoonPrice();
        this.eveningPrice = weddingHall.getEveningPrice();
        this.weekendPrice = weddingHall.getWeekendPrice();
        this.status = weddingHall.getStatus();
        this.createdTime = weddingHall.getCreatedTime();
    }

    public static Page<WeddingHallRequestDto> fromWeddingHalls(Page<WeddingHall> weddingHalls, Pageable pageable) {
        return new PageImpl<>(
                weddingHalls.getContent()
                        .stream()
                        .map(WeddingHallRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                weddingHalls.getTotalElements()
        );
    }

    public static List<WeddingHallRequestDto> fromWeddingHalls(List<WeddingHall> weddingHalls) {
        return
                weddingHalls.stream()
                        .map(WeddingHallRequestDto::new)
                        .collect(Collectors.toList()
                        );

    }
}
