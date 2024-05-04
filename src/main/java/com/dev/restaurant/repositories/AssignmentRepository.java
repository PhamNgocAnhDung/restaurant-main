package com.dev.restaurant.repositories;


import com.dev.restaurant.models.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query(value = """
            SELECT * FROM assignments a WHERE a.wedding_party_id = :weddingPartyId
            """,
            countQuery = """
                    SELECT COUNT(a.id) FROM assignments a WHERE a.wedding_party_id = :weddingPartyId       
                    """, nativeQuery = true)
    Page<Assignment> findByWeddingPartyId(@Param("weddingPartyId") int weddingPartyId, Pageable pageable);
}