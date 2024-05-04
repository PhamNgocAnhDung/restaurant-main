package com.dev.restaurant.repositories;


import com.dev.restaurant.models.WeddingHall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface WeddingHallRepository extends JpaRepository<WeddingHall, Integer> {
    @Query(value = """
                SELECT u.* FROM wedding_halls u WHERE
                       (LOWER(u.weddingHallName) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (
                            (u.morningPrice >= :minPrice AND u.morningPrice <= :maxPrice) OR
                            (u.noonPrice >= :minPrice AND u.noonPrice <= :maxPrice) OR
                            (u.eveningPrice >= :minPrice AND u.eveningPrice <= :maxPrice) OR
                            (u.weekendPrice >= :minPrice AND u.weekendPrice <= :maxPrice)
                       ) 
            """, countQuery = """
                SELECT COUNT(u.id) FROM wedding_halls u WHERE
                       (LOWER(u.weddingHallName) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (
                            (u.morningPrice >= :minPrice AND u.morningPrice <= :maxPrice) OR
                            (u.noonPrice >= :minPrice AND u.noonPrice <= :maxPrice) OR
                            (u.eveningPrice >= :minPrice AND u.eveningPrice <= :maxPrice) OR
                            (u.weekendPrice >= :minPrice AND u.weekendPrice <= :maxPrice)
                       ) 
            """, nativeQuery = true)
    Page<WeddingHall> findWeddingHallsWithPaginationAndSort(
            @Param("name") String name,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
//            @Param("time") LocalDateTime time,
            Pageable pageable);
}