package com.raph_furniture.controllers;

import com.raph_furniture.dto.BadgeDto;
import com.raph_furniture.model.Badge;
import com.raph_furniture.services.BadgeService;
import com.raph_furniture.wrapper.BadgeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotations here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/badge")
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @PostMapping(path = "/addBadge", consumes = "multipart/form-data")
    public ResponseEntity<String> addHomeBadge(@ModelAttribute BadgeDto badgeDto) {
        return badgeService.addHomBadge(badgeDto);
    }

    //get home badge
    @GetMapping(path = "/getBadges")
    public ResponseEntity<List<BadgeWrapper>> getBadge() {

        return badgeService.getBadges();
    }

    //Update badge
    @PutMapping(path = "/updateBadge/{id}")
    public ResponseEntity<String> updateBadge(@ModelAttribute BadgeDto badgeDto) {
        return badgeService.updateBadge(badgeDto);
    }
}
