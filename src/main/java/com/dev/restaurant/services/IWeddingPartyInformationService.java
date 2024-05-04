package com.dev.restaurant.services;


import com.dev.restaurant.DTO.WeddingPartyInformationRequestDto;
import com.dev.restaurant.models.WeddingHallType;
import com.dev.restaurant.models.WeddingPartyInformation;
import com.dev.restaurant.models.WeddingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IWeddingPartyInformationService {


    Page<WeddingPartyInformationRequestDto> getWeddingPartyInformation(
            int id,
            LocalDateTime time,
            Long price,
            String weddingHallName,
            int brandId,
            int userId,
            String userEmail,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);


    List<WeddingService> getServicesWeddingPartyInformation(int weddingPartyInformationId);

    WeddingPartyInformationRequestDto saveWeddingPartyInformation(WeddingPartyInformation weddingPartyInformation);

    WeddingPartyInformationRequestDto addWeddingPartyInformation(int weddingHallId, List<Integer> weddingServiceIds, LocalDateTime partyDate, WeddingHallType weddingHallType, HttpServletRequest request);
}