package com.dev.restaurant.services.impl;


import com.dev.restaurant.DTO.WeddingPartyInformationRequestDto;
import com.dev.restaurant.models.User;
import com.dev.restaurant.models.WeddingHallType;
import com.dev.restaurant.models.WeddingPartyInformation;
import com.dev.restaurant.models.WeddingService;
import com.dev.restaurant.repositories.*;
import com.dev.restaurant.services.IWeddingPartyInformationService;
import com.dev.restaurant.util.JwtTokenUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WeddingPartyInformationServiceImpl implements IWeddingPartyInformationService {
    @Autowired
    private final WeddingPartyInformationRepository weddingPartyInformationRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final WeddingHallRepository weddingHallRepository;
    @Autowired
    private final WeddingServiceRepository weddingServiceRepository;
    @Autowired
    private final TokenRepository tokenRepository;

    @Override
    public Page<WeddingPartyInformationRequestDto> getWeddingPartyInformation(
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
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
        return WeddingPartyInformationRequestDto.fromWeddingPartyInformations(
                weddingPartyInformationRepository
                        .findWeddingPartyInformationsWithPaginationAndSort(id, time, price, weddingHallName, brandId, userId, userEmail, pageable)
                , pageable);
    }


    @Override
    public List<WeddingService> getServicesWeddingPartyInformation(int weddingPartyInformationId) {
        return weddingPartyInformationRepository
                .findById(weddingPartyInformationId)
                .map(w -> {
                            if (!Objects.equals(w.getWeddingServiceIds(), "") && w.getWeddingServiceIds() != null)
                                return weddingServiceRepository.getByListIds(
                                        Arrays.stream(w.getWeddingServiceIds().split(","))
                                                .map(Integer::parseInt).toList());
                            else
                                return new ArrayList<WeddingService>();
                        }
                ).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public WeddingPartyInformationRequestDto saveWeddingPartyInformation(WeddingPartyInformation weddingPartyInformation) {
        return new WeddingPartyInformationRequestDto(weddingPartyInformationRepository.save(weddingPartyInformation));
    }

    @Override
    public WeddingPartyInformationRequestDto addWeddingPartyInformation(int weddingHallId, List<Integer> weddingServiceIds, LocalDateTime partyDate, WeddingHallType weddingHallType, HttpServletRequest request) {
        Long[] totalPrice = {0L}; // Sử dụng một mảng để lưu giá trị totalPrice
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(tokenRepository);
        User user = userRepository.findByEmail(jwtTokenUtil.extractUserName(request.getHeader("Authorization").substring(7))).orElse(null);
        List<WeddingService> weddingS = weddingServiceRepository
                .getByListIds(weddingServiceIds)
                .stream().peek(ws -> totalPrice[0] += ws.getWeddingServicePrice()).toList();

        WeddingPartyInformation wpi = weddingPartyInformationRepository.save(WeddingPartyInformation.builder()
                .partyDate(partyDate)
                .weddingHallType(weddingHallType)
                .partyHost(user)
                .weddingHall(weddingHallRepository.findById(weddingHallId)
                        .map(w -> {
                            totalPrice[0] += weddingHallType == WeddingHallType.MORNING
                                    ? w.getMorningPrice() : weddingHallType == WeddingHallType.NOON
                                    ? w.getNoonPrice() : weddingHallType == WeddingHallType.EVENING
                                    ? w.getEveningPrice() : w.getWeekendPrice();
                            return w;
                        }).orElseThrow(() -> new RuntimeException("Wedding Hall not found")))
                .weddingServices(
                        weddingS
                )
                .weddingServiceIds(weddingServiceIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")))
                .totalPrice(totalPrice[0])
                .build());
//        weddingServiceRepository.saveAll(
//                weddingS.stream().peek(ws -> ws.setWeddingPartyInformations(Collections.singletonList(wpi))).toList()
//        );
        return new WeddingPartyInformationRequestDto(wpi);
    }

}
