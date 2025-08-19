package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.LoginDto;
import com.raph_furniture.dto.UserDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.jwt.JwtUtil;
import com.raph_furniture.repository.UserRepository;
import com.raph_furniture.services.UserService;
import com.raph_furniture.utils.FurnitureUtils;
import com.raph_furniture.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.raph_furniture.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add your annotations here
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    public ResponseEntity<String> register(UserDto userDto) {
        try {
            log.info("Inside try of register function");
            if (validatedRegisterMap(userDto)) {
                if (!userRepository.existsByEmail(userDto.getEmail())) {
                    User user = new User();
                    user.setName(userDto.getName());
                    user.setEmail(userDto.getEmail());
                    user.setContact(userDto.getContact());
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    user.setRole("user"); // <= CHANGED

                    userRepository.save(user);
                    return FurnitureUtils.getResponseEntity("User registered successfully", HttpStatus.CREATED);
                } else {
                    return FurnitureUtils.getResponseEntity("Email already exists.", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> login(LoginDto loginDto) {
        try {
            log.info("Inside try of login function");
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(loginDto.getEmail()), userDetailsService.getUserDetail().getRole());

                return new ResponseEntity<String>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Credentials do not match.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Implement validate register here
    private boolean validatedRegisterMap(UserDto userDto) {
        return userDto.getName() != null && userDto.getEmail() != null && userDto.getContact() != null && userDto.getPassword() != null;
    }

    //implement the get all users method here
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (jwtFilter.currentUserHasRole("ADMIN")) {

                String currentUserEmail = SecurityContextHolder.getContext().getAuthentication() != null
                        ? SecurityContextHolder.getContext().getAuthentication().getName()
                        : null;


                List<User> users = userRepository.findAll();
                List<UserWrapper> wrappers = new ArrayList<>();
                for (User user : users) {
                    if (!user.getEmail().equalsIgnoreCase(currentUserEmail)) { // Skip logged-in admin
                        wrappers.add(new UserWrapper(
                                user.getId().intValue(),
                                user.getName(),
                                user.getEmail(),
                                user.getContact(),
                                user.getRole()
                        ));
                    }
                }
                return new ResponseEntity<>(wrappers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Long id, UserDto userDto) {
        try {
            if (jwtFilter.currentUserHasRole("ADMIN")) {

                Optional<User> optionalUser = userRepository.findById(id);

                if (optionalUser.isPresent()) {
                    User existingUser = optionalUser.get();

                    existingUser.setName(userDto.getName());
                    existingUser.setEmail(userDto.getEmail());
                    existingUser.setContact(userDto.getContact());


                    userRepository.save(existingUser);

                    return new ResponseEntity<>("User updated successfully.", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("User id does not exist.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateRole(Long id, UserDto userDto) {
        try {
            if (jwtFilter.currentUserHasRole("Admin")) {
                Optional<User> updatedUser = userRepository.findById(id);
                if (updatedUser.isPresent()) {
                    User updateUser = updatedUser.get();

                    updateUser.setRole(userDto.getRole());

                    //Save the updated user role
                    userRepository.save(updateUser);

                    return FurnitureUtils.getResponseEntity("User role updated successfully.", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("User id does not exist.", HttpStatus.OK);
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        try {
            if (jwtFilter.currentUserHasRole("ADMIN")) {
                Optional<User> deleteUser = userRepository.findById(id);

                if (deleteUser.isPresent()) {
                    userRepository.delete(deleteUser.get());
                    return FurnitureUtils.getResponseEntity("User deleted successfully.", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("User id does not exist.", HttpStatus.NOT_FOUND);
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
