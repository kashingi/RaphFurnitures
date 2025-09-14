package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.CartDao;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Cart;
import com.raph_furniture.model.Product;
import com.raph_furniture.model.User;
import com.raph_furniture.repository.CartRepository;
import com.raph_furniture.repository.ProductRepository;
import com.raph_furniture.repository.UserRepository;
import com.raph_furniture.services.CartService;
import com.raph_furniture.utils.FurnitureUtils;
import com.raph_furniture.wrapper.CartWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add your imports here
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addToCart(CartDao cartDao) {
        try {
            String email = jwtFilter.getCurrentUser();
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent()) {
                Optional<Product> product = productRepository.findById(cartDao.getProductId());
                if (product.isPresent()) {
                    if (product.get().getStatus().equals("false")) {
                        return FurnitureUtils.getResponseEntity("Product is not available", HttpStatus.BAD_REQUEST);
                    }
                    if (cartDao.getQuantity() <= 0) {
                        return FurnitureUtils.getResponseEntity("Quantity must be greater than 0", HttpStatus.BAD_REQUEST);
                    }
                    if (cartRepository.existsByUserIdAndProductId(user.get().getId(), cartDao.getProductId())) {
                        return FurnitureUtils.getResponseEntity("Product already in cart", HttpStatus.BAD_REQUEST);
                    }

                    //Add to cart
                    Cart cart = new Cart();

                    cart.setUser(user.get());
                    cart.setProduct(product.get());
                    cart.setQuantity(cartDao.getQuantity());

                    //Save the set cart
                    cartRepository.save(cart);

                    return FurnitureUtils.getResponseEntity("Product added to cart successfully.", HttpStatus.CREATED);
                } else {
                    return FurnitureUtils.getResponseEntity("Product not found.", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return FurnitureUtils.getResponseEntity("User does not exist.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CartWrapper>> getCart() {
        try {
            String email = jwtFilter.getCurrentUser();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                List<CartWrapper> cartItems = cartRepository.findByUserId(user.get().getId());

                return new ResponseEntity<>(cartItems, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCart(Long id, CartDao cartDao) {
        try {
            String email = jwtFilter.getCurrentUser();
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent()) {
                Optional<Cart> optionalCart = cartRepository.findById(id);
                if (optionalCart.isPresent()) {
                    Cart cart = optionalCart.get();

                    if (!cart.getUser().getId().equals(user.get().getId())) {
                        return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                    }
                    if (cartDao.getQuantity() <= 0) {
                        return FurnitureUtils.getResponseEntity("Quantity must be greater than 0", HttpStatus.BAD_REQUEST);
                    }
                    //update cart
                    cart.setQuantity(cartDao.getQuantity());

                    //save updates
                    cartRepository.save(cart);

                    return FurnitureUtils.getResponseEntity("Cart updated successfully", HttpStatus.OK);
                } else {
                    return FurnitureUtils.getResponseEntity("Cart item not found", HttpStatus.NOT_FOUND);
                }
            } else {
                return FurnitureUtils.getResponseEntity("User not found", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeFromCart(Long id) {
        try {
            String email = jwtFilter.getCurrentUser();
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                return FurnitureUtils.getResponseEntity("User not found", HttpStatus.UNAUTHORIZED);
            }
            Optional<Cart> optionalCart = cartRepository.findById(id);
            if (optionalCart.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Cart item not found", HttpStatus.NOT_FOUND);
            }

            Cart cart = optionalCart.get();
            if (!cart.getUser().getId().equals(user.get().getId())) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            //Remove the cart item
            cartRepository.delete(cart);

            return FurnitureUtils.getResponseEntity("Cart item deleted successfully", HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
