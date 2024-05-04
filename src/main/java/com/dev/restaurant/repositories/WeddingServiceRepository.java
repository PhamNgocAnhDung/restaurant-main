package com.dev.restaurant.repositories;


import com.dev.restaurant.models.WeddingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface WeddingServiceRepository extends JpaRepository<WeddingService, Integer> {
    @Query(value = """
                SELECT u.* FROM wedding_services u WHERE
                       (LOWER(u.weddingServiceName) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (u.weddingServicePrice >= :minPrice AND u.weddingServicePrice <= :maxPrice)
            """, countQuery = """
                SELECT COUNT(u.id) FROM wedding_services u WHERE
                       (LOWER(u.weddingServiceName) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (u.weddingServicePrice >= :minPrice AND u.weddingServicePrice <= :maxPrice)
            """, nativeQuery = true)
    Page<WeddingService> findWeddingServicesWithPaginationAndSort(
            @Param("name") String name,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable);

    @Query(value = """
            SELECT * FROM wedding_services u WHERE u.id IN :ids
            """, nativeQuery = true)
    List<WeddingService> getByListIds(
            @Param("ids") List<Integer> ids
    );
}