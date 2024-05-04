package com.dev.restaurant.repositories;


import com.dev.restaurant.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

}