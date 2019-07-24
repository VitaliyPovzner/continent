package com.povzner.continent.demo.interfaces;

import com.povzner.continent.demo.entity.Travel;
import org.springframework.http.ResponseEntity;

public interface ITravel {
    ResponseEntity<Long> createTravel(Travel  travel);
    ResponseEntity<Travel> updateTravel(Travel travel,Long id);
     ResponseEntity<Travel> getTravelById(Long id);
}
