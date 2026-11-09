
package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.MarketPrice;
import java.util.List;

public interface MarketPriceRepository
        extends JpaRepository<MarketPrice, Long> {

    List<MarketPrice> findByCropNameIgnoreCase(String cropName);

}
