package com.povzner.continent.demo.controller;

import com.povzner.continent.demo.entity.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.TravelService;

@RestController
public class TravelController {
    @Autowired
    TravelService service;
    @PostMapping()
    public ResponseEntity<Long> createTravel(@RequestBody Travel travel){
        return service.createTravel(travel);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<Travel> updateTravel(@RequestBody Travel travel,@PathVariable Long id){
        return service.updateTravel(travel,id);
    }
    @GetMapping
    public ResponseEntity<Travel> getById(@RequestParam Long id){
        return service.getTravelById(id);
    }
}
