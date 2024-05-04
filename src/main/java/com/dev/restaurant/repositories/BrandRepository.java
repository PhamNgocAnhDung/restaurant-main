package com.dev.restaurant.repositories;


import com.dev.restaurant.models.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query(value = """
                SELECT u.* FROM brands u WHERE
                       (LOWER(u.brandName) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (LOWER(u.brandAddress) LIKE LOWER(CONCAT('%', :address, '%')))
            """, countQuery = """
                SELECT COUNT(u.id) FROM brands u WHERE
                       (LOWER(u.brandName) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (LOWER(u.brandAddress) LIKE LOWER(CONCAT('%', :address, '%')))
            """, nativeQuery = true)
    Page<Brand> findBrandsWithPaginationAndSort(@Param("name") String name, @Param("address") String address, Pageable pageable);
}