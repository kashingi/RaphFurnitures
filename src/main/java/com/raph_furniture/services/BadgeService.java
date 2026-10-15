package com.raph_furniture.services;

import com.raph_furniture.dto.BadgeDto;
import com.raph_furniture.model.Badge;
import com.raph_furniture.wrapper.BadgeWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Add your annotations here
public interface BadgeService {
    ResponseEntity<String> addHomBadge(BadgeDto badgeDto);

    ResponseEntity<List<BadgeWrapper>> getBadges();

    ResponseEntity<String> updateBadge(BadgeDto badgeDto);
}
