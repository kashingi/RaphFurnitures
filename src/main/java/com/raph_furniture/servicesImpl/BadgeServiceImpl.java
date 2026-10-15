package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.BadgeDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Badge;
import com.raph_furniture.repository.BadgeRepository;
import com.raph_furniture.services.BadgeService;
import com.raph_furniture.utils.FurnitureUtils;
import com.raph_furniture.wrapper.BadgeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add your annotations here
@Service
public class BadgeServiceImpl implements BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addHomBadge(BadgeDto badgeDto) {
        try {
            Badge badge = new Badge();
            badge.setTitle(StringUtils.cleanPath(badgeDto.getTitle()));
            badge.setBadgeName(StringUtils.cleanPath(badgeDto.getBadgeName()));
            badge.setDescription(StringUtils.cleanPath(badgeDto.getDescription()));

            MultipartFile file = badgeDto.getImage();
            if (file == null && file.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Image is required for a new badge", HttpStatus.BAD_REQUEST);
            }
            badge.setImage(file.getBytes());

            badgeRepository.save(badge);

            return FurnitureUtils.getResponseEntity("Badge uploaded successfully", HttpStatus.CREATED);
        } catch (IOException ex) {
            ex.printStackTrace();
            return FurnitureUtils.getResponseEntity("File processing error", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();

            return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<BadgeWrapper>> getBadges() {
        try {
            List<BadgeWrapper> badgeItems = badgeRepository.fetchAllBadges();

            return new ResponseEntity<>(badgeItems, HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateBadge(BadgeDto badgeDto) {
        try {
            if (jwtFilter.isUser()) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (badgeDto.getId() == null) {
                return FurnitureUtils.getResponseEntity("Badge id does not exist", HttpStatus.BAD_REQUEST);
            }

            Optional<Badge> optionalBadge = badgeRepository.findById(badgeDto.getId());
            if (optionalBadge.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Badge not found with id : " + badgeDto.getId(), HttpStatus.NOT_FOUND);
            }

            Badge badge = optionalBadge.get();

            if (badgeDto.getTitle() != null && !badgeDto.getTitle().isEmpty()) {
                badge.setTitle(badgeDto.getTitle());
            }
            if (badgeDto.getBadgeName() != null && !badgeDto.getBadgeName().isEmpty()) {
                badge.setBadgeName(badgeDto.getBadgeName());
            }
            if (badgeDto.getDescription() != null && !badgeDto.getDescription().isEmpty()) {
                badge.setDescription(badgeDto.getDescription());
            }
            //update image only if new file is uploaded
            MultipartFile file = badgeDto.getImage();
            if (file != null && !file.isEmpty()) {
                badge.setImage(file.getBytes());
            }
            //Save the new badge
            badgeRepository.save(badge);
            return FurnitureUtils.getResponseEntity("Badge updated successfully", HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();

            return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
