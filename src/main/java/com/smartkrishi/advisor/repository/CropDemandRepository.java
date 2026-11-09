
package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.CropDemand;

public interface CropDemandRepository
        extends JpaRepository<CropDemand, Long> {

}
