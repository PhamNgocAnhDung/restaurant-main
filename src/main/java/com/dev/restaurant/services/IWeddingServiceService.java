package com.dev.restaurant.services;


import com.dev.restaurant.DTO.WeddingServiceRequestDto;
import com.dev.restaurant.models.WeddingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface IWeddingServiceService {


    Page<WeddingServiceRequestDto> getWeddingService(
            String name,
            Long minPrice,
            Long maxPrice,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);

    WeddingServiceRequestDto saveWeddingService(WeddingService weddingService);

    WeddingServiceRequestDto updateWeddingService(WeddingService weddingService);

    WeddingServiceRequestDto changeStatusWeddingService(int id);
}