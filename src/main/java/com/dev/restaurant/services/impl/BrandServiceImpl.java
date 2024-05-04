package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.BrandRequestDto;
import com.dev.restaurant.models.Brand;
import com.dev.restaurant.models.Status;
import com.dev.restaurant.repositories.BrandRepository;
import com.dev.restaurant.services.IBrandService;
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
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private final BrandRepository brandRepository;

    @Override
    public Page<BrandRequestDto> getBrand(String name, String address, int page, int limit, String field, String typeSort, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));

        return BrandRequestDto.fromBrands(brandRepository.findBrandsWithPaginationAndSort(name, address, pageable), pageable);
    }

    @Override
    public BrandRequestDto saveBrand(Brand brand) {
        return new BrandRequestDto(brandRepository.save(brand));
    }

    @Override
    public BrandRequestDto updateBrand(Brand brand) {
        return new BrandRequestDto(brandRepository.save(brand));
    }

    @Override
    public BrandRequestDto changeStatusBrand(int id) {
        return new BrandRequestDto(brandRepository.findById(id).map(b -> {
            b.setStatus(b.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
            return brandRepository.save(b);
        }).orElseThrow(() -> new RuntimeException("Brand not found")));
    }
}
