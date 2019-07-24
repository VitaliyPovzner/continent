package com.povzner.continent.demo.repository;

import com.povzner.continent.demo.entity.Travel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends CrudRepository<Travel,Long> {
}
