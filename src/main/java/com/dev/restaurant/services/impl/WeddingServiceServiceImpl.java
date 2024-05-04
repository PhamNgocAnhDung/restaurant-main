package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.WeddingServiceRequestDto;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.models.WeddingService;
import com.dev.restaurant.repositories.WeddingServiceRepository;
import com.dev.restaurant.services.IWeddingServiceService;
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
public class WeddingServiceServiceImpl implements IWeddingServiceService {
    @Autowired
    private final WeddingServiceRepository weddingServiceRepository;


    @Override
    public Page<WeddingServiceRequestDto> getWeddingService(
            String name,
            Long minPrice,
            Long maxPrice,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));

        return WeddingServiceRequestDto.fromWeddingServices(weddingServiceRepository.findWeddingServicesWithPaginationAndSort(name, minPrice, maxPrice, pageable), pageable);
    }

    @Override
    public WeddingServiceRequestDto saveWeddingService(WeddingService weddingService) {
        return new WeddingServiceRequestDto(weddingServiceRepository.save(weddingService));
    }

    @Override
    public WeddingServiceRequestDto updateWeddingService(WeddingService weddingService) {
        return new WeddingServiceRequestDto(weddingServiceRepository.save(weddingService));
    }

    @Override
    public WeddingServiceRequestDto changeStatusWeddingService(int id) {
        return new WeddingServiceRequestDto(weddingServiceRepository.findById(id).map(w -> {
            w.setStatus(w.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
            return weddingServiceRepository.save(w);
        }).orElseThrow(() -> new RuntimeException("Wedding Hall not found")));
    }
}
