package com.dev.restaurant.repositories;


import com.dev.restaurant.models.WeddingPartyInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

@EnableJpaRepositories
public interface WeddingPartyInformationRepository extends JpaRepository<WeddingPartyInformation, Integer> {

    @Query(value = """
                SELECT u.* FROM wedding_party_informations u WHERE
                       (:id = -1 OR u.id = :id) AND
                       (
                          :time IS NULL OR
                          (:time <= u.partyBookingDate AND u.partyBookingDate < DATE_ADD(:time, INTERVAL 1 DAY)) OR
                          (:time <= u.partyDate AND u.partyDate < DATE_ADD(:time, INTERVAL 1 DAY))              
                       ) AND
                       (:price = -1 OR u.totalPrice = :price) AND
                       (
                          :weddingHallName = '' OR
                          (
                             u.id IN
                              (
                                SELECT wpi.id FROM wedding_halls w, wedding_party_informations wpi
                                WHERE w.id = wpi.wedding_hall_Id AND
                                      LOWER(w.weddingHallName) LIKE LOWER(CONCAT('%', :weddingHallName, '%'))
                              )
                          )
                       ) AND
                       (
                          :brandId = -1 OR
                          (
                             u.id IN
                              (
                                SELECT wpi.id FROM wedding_halls w, wedding_party_informations wpi
                                WHERE w.id = wpi.wedding_hall_Id AND
                                      w.brand_id = :brandId
                              )
                          )
                       ) AND
                       (:userId = -1 OR u.party_host_id = :userId) AND
                       (
                            :userEmail = '' OR
                            u.id IN (
                                SELECT wpi.id FROM users us, wedding_party_informations wpi
                                WHERE us.id = wpi.party_host_id AND us.email = :userEmail
                              ) 
                       )
            """, countQuery = """
                SELECT COUNT(u.id) FROM wedding_party_informations u WHERE
                       (:id = -1 OR u.id = :id) AND
                       (
                          :time IS NULL OR
                          (:time <= u.partyBookingDate AND u.partyBookingDate < DATE_ADD(:time, INTERVAL 1 DAY)) OR
                          (:time <= u.partyDate AND u.partyDate < DATE_ADD(:time, INTERVAL 1 DAY))              
                       ) AND
                       (:price = -1 OR u.totalPrice = :price) AND
                       (
                          :weddingHallName = '' OR
                          (
                             u.id IN
                              (
                                SELECT wpi.id FROM wedding_halls w, wedding_party_informations wpi
                                WHERE w.id = wpi.wedding_hall_Id AND
                                      LOWER(w.weddingHallName) LIKE LOWER(CONCAT('%', :weddingHallName, '%'))
                              )
                          )
                       ) AND
                       (
                          :brandId = -1 OR
                          (
                             u.id IN
                              (
                                SELECT wpi.id FROM wedding_halls w, wedding_party_informations wpi
                                WHERE w.id = wpi.wedding_hall_Id AND
                                      w.brand_id = :brandId
                              )
                          )
                       ) AND
                       (:userId = -1 OR u.party_host_id = :userId) AND
                       (
                            :userEmail = '' OR
                            u.id IN (
                                SELECT wpi.id FROM users us, wedding_party_informations wpi
                                WHERE us.id = wpi.party_host_id AND us.email = :userEmail
                              ) 
                       )
            """, nativeQuery = true)
    Page<WeddingPartyInformation> findWeddingPartyInformationsWithPaginationAndSort(
            @Param("id") int id,
            @Param("time") LocalDateTime time,
            @Param("price") Long price,
            @Param("weddingHallName") String weddingHallName,
            @Param("brandId") int brandId,
            @Param("userId") int userId,
            @Param("userEmail") String userEmail,
            Pageable pageable);
}