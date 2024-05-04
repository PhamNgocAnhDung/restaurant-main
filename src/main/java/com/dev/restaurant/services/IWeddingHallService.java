package com.dev.restaurant.services;


import com.dev.restaurant.DTO.WeddingHallRequestDto;
import com.dev.restaurant.models.WeddingHall;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface IWeddingHallService {


    Page<WeddingHallRequestDto> getWeddingHall(
            String name,
            Long minPrice,
            Long maxPrice,
            LocalDateTime time,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);

    WeddingHallRequestDto saveWeddingHall(WeddingHall weddingHall);

    WeddingHallRequestDto updateWeddingHall(WeddingHall weddingHall);

    WeddingHallRequestDto changeStatusWeddingHall(int id);
}