package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.WeddingHallRequestDto;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.WeddingHall;
import com.dev.restaurant.repositories.WeddingHallRepository;
import com.dev.restaurant.services.IWeddingHallService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class WeddingHallServiceImpl implements IWeddingHallService {
    @Autowired
    private final WeddingHallRepository weddingHallRepository;


    @Override
    public Page<WeddingHallRequestDto> getWeddingHall(
            String name,
            Long minPrice,
            Long maxPrice,
            LocalDateTime time,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));

        return WeddingHallRequestDto.fromWeddingHalls(weddingHallRepository.findWeddingHallsWithPaginationAndSort(name, minPrice, maxPrice, pageable), pageable);
    }

    @Override
    public WeddingHallRequestDto saveWeddingHall(WeddingHall weddingHall) {
        return new WeddingHallRequestDto(weddingHallRepository.save(weddingHall));
    }

    @Override
    public WeddingHallRequestDto updateWeddingHall(WeddingHall weddingHall) {
        return new WeddingHallRequestDto(weddingHallRepository.save(weddingHall));
    }

    @Override
    public WeddingHallRequestDto changeStatusWeddingHall(int id) {
        return new WeddingHallRequestDto(weddingHallRepository.findById(id).map(w -> {
            w.setStatus(w.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
            return weddingHallRepository.save(w);
        }).orElseThrow(() -> new RuntimeException("Wedding Hall not found")));
    }
}
