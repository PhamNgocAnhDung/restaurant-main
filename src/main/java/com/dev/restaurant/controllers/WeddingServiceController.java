package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.WeddingServiceRequestDto;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.models.WeddingService;
import com.dev.restaurant.services.IWeddingServiceService;
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
public class WeddingServiceController {
    private final IWeddingServiceService weddingServiceService;


    @GetMapping("/wedding_service")
    public ResponseObject<Page<WeddingServiceRequestDto>> get(
            @RequestParam(value = "_name", defaultValue = "") String name,
            @RequestParam(value = "_min_price", defaultValue = "0") Long minPrice,
            @RequestParam(value = "_max_price", defaultValue = "") Long maxPrice,
            @RequestParam(value = "_page", defaultValue = "0") int page,
            @RequestParam(value = "_limit", defaultValue = "20") int limit,
            @RequestParam(value = "_field", defaultValue = "id") String field,
            @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
            HttpServletRequest request
    ) {
        Page<WeddingServiceRequestDto> weddingHalls = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            weddingHalls = weddingServiceService.getWeddingService
                    (name, minPrice, maxPrice == null ? Long.MAX_VALUE : maxPrice, page, limit, field, typeSort, request);
            if (weddingHalls.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found WeddingServices.",
                        weddingHalls
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        weddingHalls
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get WeddingServices with filter. " + exception.getMessage(),
                    weddingHalls
            );
        }
    }


    @PostMapping("/wedding_service")
    public ResponseObject<WeddingServiceRequestDto> saveWeddingService(
            @RequestBody WeddingService weddingService
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Wedding Service Successfully.",
                    weddingServiceService.saveWeddingService(weddingService)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Wedding Service. " + exception.getMessage(),
                    null
            );
        }
    }


    @PutMapping("/wedding_service")
    public ResponseObject<WeddingServiceRequestDto> updateWeddingService(
            @RequestBody WeddingService weddingService
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update Wedding Hall Successfully.",
                    weddingServiceService.updateWeddingService(weddingService)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update WeddingHall. " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/wedding_service/change_status/{id}")
    public ResponseObject<WeddingServiceRequestDto> changeStatusWeddingService(
            @PathVariable int id
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Wedding Service Successfully.",
                    weddingServiceService.changeStatusWeddingService(id)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Wedding Serivce. " + exception.getMessage(),
                    null
            );
        }
    }

}
