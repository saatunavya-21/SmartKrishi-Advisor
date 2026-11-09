
package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.Buyer;
import java.util.Optional;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findByEmail(String email);

}
