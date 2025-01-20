package org.pineapple.pineappleapi.repository;

import org.pineapple.pineappleapi.entity.Pineapple;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PineappleRepository extends MongoRepository<Pineapple, String> {
    Optional<Pineapple> findFirstByOrderByIdAsc();
}
