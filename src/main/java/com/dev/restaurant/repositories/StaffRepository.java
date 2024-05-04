package com.dev.restaurant.repositories;


import com.dev.restaurant.models.Staff;
import com.dev.restaurant.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@EnableJpaRepositories
public interface StaffRepository extends JpaRepository<Staff, Integer> {


    Optional<Staff> findByEmail(String email);

    @Query(value = """
                SELECT u.* FROM staffs u WHERE
                       (:id = -1 OR u.id = :id) AND
                       (LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND
                       (CONCAT(LOWER(u.firstName),' ',LOWER(u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%')))
            """, countQuery = """
                SELECT COUNT(u.id) FROM staffs u WHERE
                       (:id = -1 OR u.id = :id) AND
                       (LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND
                       (CONCAT(LOWER(u.firstName),' ',LOWER(u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%')))
            """, nativeQuery = true)
    Page<Staff> findStaffsWithPaginationAndSort(
            @Param("id") int id,
            @Param("email") String email,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            Pageable pageable
    );

 }