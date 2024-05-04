package com.dev.restaurant.controllers;


import com.dev.restaurant.DTO.WeddingPartyInformationRequestDto;
import com.dev.restaurant.models.ResponseObject;
import com.dev.restaurant.models.WeddingHallType;
import com.dev.restaurant.models.WeddingPartyInformation;
import com.dev.restaurant.models.WeddingService;
import com.dev.restaurant.services.IWeddingPartyInformationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class WeddingPartyInformationController {
    @Qualifier("IWeddingPartyInformationService")
    private final IWeddingPartyInformationService weddingPartyInformationService;


    @GetMapping("/wedding_party_information")
    public ResponseObject<Page<WeddingPartyInformationRequestDto>> get(
            @RequestParam(value = "_id", defaultValue = "-1") int id,
            @RequestParam(value = "_time", defaultValue = "") String time,
            @RequestParam(value = "_price", defaultValue = "-1") Long price,
            @RequestParam(value = "_wedding_hall_name", defaultValue = "") String weddingHallName,
            @RequestParam(value = "_brand_id", defaultValue = "-1") int brandId,
            @RequestParam(value = "_user_id", defaultValue = "-1") int userId,
            @RequestParam(value = "_user_email", defaultValue = "") String userEmail,
            @RequestParam(value = "_page", defaultValue = "0") int page,
            @RequestParam(value = "_limit", defaultValue = "20") int limit,
            @RequestParam(value = "_field", defaultValue = "id") String field,
            @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
            HttpServletRequest request
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Page<WeddingPartyInformationRequestDto> weddingHalls = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            weddingHalls = weddingPartyInformationService.getWeddingPartyInformation
                    (id, Objects.equals(time, "") ? null : LocalDateTime.parse(time + " 00:00:00", formatter), price, weddingHallName, brandId, userId, userEmail, page, limit, field, typeSort, request);
            if (weddingHalls.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Wedding Party Informations.",
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
                    "Can't get Wedding Party Informations with filter. " + exception.getMessage(),
                    weddingHalls
            );
        }
    }

    @GetMapping("/wedding_party_information/{id}/get_services")
    public ResponseObject<List<WeddingService>> getServicesWeddingPartyInformation(@PathVariable int id) {
        try {

            ResponseObject<List<WeddingService>> listResponseObject = new ResponseObject<>(
                    "ok",
                    "Get success",
                    weddingPartyInformationService.getServicesWeddingPartyInformation(id)
            );
            return listResponseObject;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseObject<>(
                    "error",
                    "Can't get Wedding Party Informations with filter. " + exception.getMessage(),
                    new ArrayList<>()
            );
        }
    }

    @PostMapping("/wedding_party_information")
    public ResponseObject<WeddingPartyInformationRequestDto> saveWeddingService(
            @RequestParam("weddingHallId") int weddingHallId,
            @RequestParam("weddingServiceIds") List<Integer> weddingServiceIds,
            @RequestParam("partyDate") String partyDate,
            @RequestParam("weddingHallType") WeddingHallType weddingHallType,
            HttpServletRequest request
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Wedding Party Information Successfully.",
                    weddingPartyInformationService.addWeddingPartyInformation(weddingHallId, weddingServiceIds, LocalDateTime.parse(partyDate, formatter), weddingHallType, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Wedding Party Information. " + exception.getMessage(),
                    null
            );
        }
    }

    @PostMapping("/wedding_party_information/save")
    public ResponseObject<WeddingPartyInformationRequestDto> saveWeddingService(
            @RequestBody WeddingPartyInformation weddingPartyInformation
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Wedding Party Information Successfully.",
                    weddingPartyInformationService.saveWeddingPartyInformation(weddingPartyInformation)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add Wedding Party Information. " + exception.getMessage(),
                    null
            );
        }
    }

}
