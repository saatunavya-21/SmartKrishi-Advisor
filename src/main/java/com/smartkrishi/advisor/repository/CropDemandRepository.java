package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.CropDemand;
import java.util.List;

public interface CropDemandRepository extends JpaRepository<CropDemand, Long> {

    List<CropDemand> findByBuyerId(Long buyerId);

}
