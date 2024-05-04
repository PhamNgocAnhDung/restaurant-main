package com.dev.restaurant.DTO;


import com.dev.restaurant.models.WeddingHallType;
import com.dev.restaurant.models.WeddingPartyInformation;
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
public class WeddingPartyInformationRequestDto {
    private int id;
    private LocalDateTime partyBookingDate;
    private LocalDateTime partyDate;
    private Long totalPrice;
    private WeddingHallType weddingHallType;
    private UserRequestDto user;
    private WeddingHallRequestDto weddingHall;
    private List<WeddingServiceRequestDto> weddingServices;


    public WeddingPartyInformationRequestDto(WeddingPartyInformation weddingPartyInformation) {
        this.id = weddingPartyInformation.getId();
        this.partyBookingDate = weddingPartyInformation.getPartyBookingDate();
        this.partyDate = weddingPartyInformation.getPartyDate();
        this.totalPrice = weddingPartyInformation.getTotalPrice();
        this.weddingHallType = weddingPartyInformation.getWeddingHallType();
        this.user = new UserRequestDto(weddingPartyInformation.getPartyHost());
        this.weddingHall = new WeddingHallRequestDto(weddingPartyInformation.getWeddingHall());
        this.weddingServices = WeddingServiceRequestDto.fromWeddingServices(weddingPartyInformation.getWeddingServices());
    }

    public static Page<WeddingPartyInformationRequestDto> fromWeddingPartyInformations(Page<WeddingPartyInformation> weddingPartyInformations, Pageable pageable) {
        return new PageImpl<>(
                weddingPartyInformations.getContent()
                        .stream()
                        .map(WeddingPartyInformationRequestDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                weddingPartyInformations.getTotalElements()
        );
    }

    public static List<WeddingPartyInformationRequestDto> fromWeddingPartyInformations(List<WeddingPartyInformation> weddingPartyInformations) {
        return
                weddingPartyInformations.stream()
                        .map(WeddingPartyInformationRequestDto::new)
                        .collect(Collectors.toList()
                        );

    }
}
