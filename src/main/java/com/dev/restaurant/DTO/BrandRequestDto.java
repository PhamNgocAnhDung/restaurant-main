package com.dev.restaurant.DTO;


import com.dev.restaurant.models.Brand;
import com.dev.restaurant.models.Status;
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
public class BrandRequestDto {
    private int id;
    private String brandName;
    private String brandAddress;
    private Status status;
    private LocalDateTime createdTime;


    public BrandRequestDto(Brand brand) {
        this.id = brand.getId();
        this.brandName = brand.getBrandName();
        this.brandAddress = brand.getBrandAddress();
        this.status = brand.getStatus();
        this.createdTime = brand.getCreatedTime();
    }

    public static Page<BrandRequestDto> fromBrands(Page<Brand> brands, Pageable pageable) {
        return new PageImpl<>(
                brands.getContent()
                        .stream()
                        .map(BrandRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                brands.getTotalElements()
        );
    }

    public static List<BrandRequestDto> fromBrands(List<Brand> brands) {
        return
                brands.stream()
                        .map(BrandRequestDto::new)
                        .collect(Collectors.toList()
                        );

    }
}
