
package com.smartkrishi.advisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartkrishi.advisor.entity.Produce;
import java.util.List;

public interface ProduceRepository extends JpaRepository<Produce, Long> {

    List<Produce> findByFarmerId(Long farmerId);

    List<Produce> findAllByOrderByIdDesc();

}
