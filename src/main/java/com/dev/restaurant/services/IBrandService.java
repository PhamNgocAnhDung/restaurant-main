package com.dev.restaurant.services;


import com.dev.restaurant.DTO.BrandRequestDto;
import com.dev.restaurant.models.Brand;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface IBrandService {


    Page<BrandRequestDto> getBrand(String name, String address, int page, int limit, String field, String typeSort, HttpServletRequest request);

    BrandRequestDto saveBrand(Brand brand);

    BrandRequestDto updateBrand(Brand brand);

    BrandRequestDto changeStatusBrand(int id);
}