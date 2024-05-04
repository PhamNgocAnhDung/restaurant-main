package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.WeddingHallRequestDto;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.models.WeddingHall;
import com.dev.restaurant.services.IWeddingHallService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class WeddingHallController {
    private final IWeddingHallService weddingHallService;


    @GetMapping("/wedding_hall")
    public ResponseObject<Page<WeddingHallRequestDto>> get(
            @RequestParam(value = "_name", defaultValue = "") String name,
            @RequestParam(value = "_min_price", defaultValue = "0") Long minPrice,
            @RequestParam(value = "_max_price", defaultValue = "") Long maxPrice,
            @RequestParam(value = "_time", defaultValue = "") String time,
            @RequestParam(value = "_page", defaultValue = "0") int page,
            @RequestParam(value = "_limit", defaultValue = "20") int limit,
            @RequestParam(value = "_field", defaultValue = "id") String field,
            @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
            HttpServletRequest request
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Page<WeddingHallRequestDto> weddingHalls = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            weddingHalls = weddingHallService.getWeddingHall
                    (name, minPrice, maxPrice == null ? Long.MAX_VALUE : maxPrice, Objects.equals(time, "") ? null : LocalDateTime.parse(time + " 00:00:00", formatter), page, limit, field, typeSort, request);
            if (weddingHalls.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found WeddingHalls.",
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
                    "Can't get WeddingHalls with filter. " + exception.getMessage(),
                    weddingHalls
            );
        }
    }


    @PostMapping("/wedding_hall")
    public ResponseObject<WeddingHallRequestDto> saveWeddingHall(
            @RequestBody WeddingHall weddingHall
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Wedding Hall Successfully.",
                    weddingHallService.saveWeddingHall(weddingHall)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Wedding Hall. " + exception.getMessage(),
                    null
            );
        }
    }


    @PutMapping("/wedding_hall")
    public ResponseObject<WeddingHallRequestDto> updateWeddingHall(
            @RequestBody WeddingHall weddingHall
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update Wedding Hall Successfully.",
                    weddingHallService.updateWeddingHall(weddingHall)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update WeddingHall. " + exception.getMessage(),
                    null
            );
        }
    }

    @PutMapping("/wedding_hall/change_status/{id}")
    public ResponseObject<WeddingHallRequestDto> changeStatusWeddingHall(
            @PathVariable int id
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Wedding Hall Successfully.",
                    weddingHallService.changeStatusWeddingHall(id)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Wedding Hall. " + exception.getMessage(),
                    null
            );
        }
    }

}
