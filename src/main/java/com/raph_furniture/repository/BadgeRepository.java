package com.raph_furniture.repository;

import com.raph_furniture.model.Badge;
import com.raph_furniture.wrapper.BadgeWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//Add your annotations here
@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    @Query(name = "Badge.GetBadges")
    List<BadgeWrapper> fetchAllBadges();
}
