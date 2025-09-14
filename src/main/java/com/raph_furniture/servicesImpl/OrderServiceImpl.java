package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.OrderDto;
import com.raph_furniture.dto.OrderStatusDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Cart;
import com.raph_furniture.model.Order;
import com.raph_furniture.model.User;
import com.raph_furniture.repository.CartRepository;
import com.raph_furniture.repository.OrderRepository;
import com.raph_furniture.repository.UserRepository;
import com.raph_furniture.services.OrderService;
import com.raph_furniture.utils.FurnitureUtils;
import com.raph_furniture.wrapper.OrderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add your annotations here
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> placeOrder(OrderDto orderDto) {
        try {

            String email = jwtFilter.getCurrentUser();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return FurnitureUtils.getResponseEntity("User not found", HttpStatus.UNAUTHORIZED);
            }

            Optional<Cart> optionalCart = cartRepository.findById(orderDto.getCartId());
            if (!optionalCart.isPresent()) {
                return FurnitureUtils.getResponseEntity("Cart item not found", HttpStatus.NOT_FOUND);
            }

            Cart cart = optionalCart.get();
            if (!cart.getUser().getId().equals(user.get().getId())) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (!isValidPaymentMethod(orderDto.getPaymentMethod())) {
                return FurnitureUtils.getResponseEntity("Invalid payment method", HttpStatus.BAD_REQUEST);
            }

            Order order = new Order();
            order.setUser(cart.getUser());
            order.setProduct(cart.getProduct());
            order.setQuantity(cart.getQuantity());
            order.setTotalAmount(cart.getProduct().getPrice() * cart.getQuantity());
            order.setPaymentMethod(orderDto.getPaymentMethod());
            order.setPaymentStatus("PENDING");
            order.setOrderStatus("PENDING");
            order.setOrderDate(LocalDateTime.now());

            //save the new order and remove from cart after order is placed
            orderRepository.save(order);
            cartRepository.delete(cart);

            return FurnitureUtils.getResponseEntity("Order placed successfully", HttpStatus.CREATED);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<OrderWrapper>> getOrders() {
        try {
            String email = jwtFilter.getCurrentUser();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

            List<OrderWrapper> orders = orderRepository.findByUserId(user.get().getId());
            return new ResponseEntity<>(orders, HttpStatus.OK);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateOrderStatus(Long id, OrderStatusDto statusDto) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Order> optionalOrder = orderRepository.findById(id);
                if (optionalOrder.isEmpty()) {
                    return FurnitureUtils.getResponseEntity("Order not found", HttpStatus.NOT_FOUND);
                }

                String status = statusDto.getStatus();
                if (!isValidOrderStatus(status)) {
                    return FurnitureUtils.getResponseEntity("Invalid order status", HttpStatus.BAD_REQUEST);
                }

                Order order = optionalOrder.get();
                order.setOrderStatus(status);
                orderRepository.save(order);

                return FurnitureUtils.getResponseEntity("Order status updated successfully", HttpStatus.OK);

            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && (
                paymentMethod.equals("CREDIT_CARD") ||
                        paymentMethod.equals("MPESA") ||
                        paymentMethod.equals("PAYBILL") ||
                        paymentMethod.equals("CASH")
        );
    }

    private boolean isValidOrderStatus(String status) {
        return status != null && (
                status.equals("PENDING") || status.equals("CONFIRMED") ||
                        status.equals("SHIPPED") || status.equals("DELIVERED") ||
                        status.equals("CANCELLED")
        );
    }
}
