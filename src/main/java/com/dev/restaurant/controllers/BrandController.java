package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.BrandRequestDto;
import com.dev.restaurant.models.Brand;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.services.IBrandService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class BrandController {
    private final IBrandService brandService;


    @GetMapping("/brand")
    public ResponseObject<Page<BrandRequestDto>> getBrand(
            @RequestParam(value = "_name", defaultValue = "") String name,
            @RequestParam(value = "_address", defaultValue = "") String address,
            @RequestParam(value = "_page", defaultValue = "0") int page,
            @RequestParam(value = "_limit", defaultValue = "20") int limit,
            @RequestParam(value = "_field", defaultValue = "id") String field,
            @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
            HttpServletRequest request
    ) {
        Page<BrandRequestDto> brands = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            brands = brandService.getBrand(name, address, page, limit, field, typeSort, request);
            if (brands.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Brands.",
                        brands
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        brands
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Brands with filter. " + exception.getMessage(),
                    brands
            );
        }
    }


    @PostMapping("/brand")
    public ResponseObject<BrandRequestDto> saveBrand(
            @RequestBody Brand brand
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Brand Successfully.",
                    brandService.saveBrand(brand)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Brand. " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/brand")
    public ResponseObject<BrandRequestDto> updateBrand(
            @RequestBody Brand brand
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update Assignment Successfully.",
                    brandService.updateBrand(brand)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update Assignment. " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/brand/change_status/{id}")
    public ResponseObject<BrandRequestDto> changeStatusBrand(
            @PathVariable int id
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Assignments Successfully.",
                    brandService.changeStatusBrand(id)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Assignments. " + exception.getMessage(),
                    null
            );
        }
    }

}
