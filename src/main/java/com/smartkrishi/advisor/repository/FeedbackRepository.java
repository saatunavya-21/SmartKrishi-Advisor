package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.Feedback;

public interface FeedbackRepository
        extends JpaRepository<Feedback, Long> {
}