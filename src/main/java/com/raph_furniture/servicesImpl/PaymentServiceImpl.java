package com.raph_furniture.servicesImpl;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.PaymentDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Order;
import com.raph_furniture.model.Payment;
import com.raph_furniture.model.User;
import com.raph_furniture.repository.OrderRepository;
import com.raph_furniture.repository.PaymentRepository;
import com.raph_furniture.repository.UserRepository;
import com.raph_furniture.services.PaymentService;
import com.raph_furniture.utils.FurnitureUtils;
import com.raph_furniture.utils.PaymentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtFilter jwtFilter;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${mpesa.consumer.key}")
    private String consumerKey;

    @Value("${mpesa.consumer.secret}")
    private String consumerSecret;

    @Value("${mpesa.shortcode}")
    private String shortcode;

    @Value("${mpesa.passkey}")
    private String passKey;

    @Value("${mpesa.callback.url}")
    private String callbackUrl;

    @Value("${mpesa.base.url}")
    private String baseUrl;

    @Value("${africastalking.username}")
    private String africastalkingUsername;

    @Value("${africastalking.api.key}")
    private String africastalkingApiKey;

    private String getAccessToken() {
        try {
            String auth = consumerKey + ":" + consumerSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encodedAuth); // Fixed: Use "Basic" instead of "Bearer"

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // M-Pesa Daraja API uses GET for token generation
            String tokenUrl = baseUrl + "/oauth/v1/generate?grant_type=client_credentials";
            log.info("Requesting access token from: {}", tokenUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("Token response: {}", response.getBody());
            JsonNode node = objectMapper.readTree(response.getBody());
            return node.get("access_token").asText();
        } catch (Exception ex) {
            log.error("Failed to get access token: {}", ex.getMessage());
            throw new RuntimeException("Failed to get access token", ex);
        }
    }

    @Override
    public ResponseEntity<String> initiateStkPush(PaymentDto paymentDto) {
        Optional<Order> optionalOrder = orderRepository.findById(paymentDto.getOrderId());
        if (optionalOrder.isEmpty()) {
            log.error("Order not found for ID: {}", paymentDto.getOrderId());
            return FurnitureUtils.getResponseEntity("Order not found", HttpStatus.BAD_REQUEST);
        }

        Order order = optionalOrder.get();
        if (!order.getPaymentStatus().equalsIgnoreCase("PENDING")) {
            log.error("Invalid order status: {}", order.getPaymentStatus());
            return FurnitureUtils.getResponseEntity("Kindly confirm order status", HttpStatus.BAD_REQUEST);
        }

        // Save pending payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPhoneNumber(paymentDto.getPhoneNumber());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        try {
            String token = getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            String timestamp = PaymentUtils.generateTimestamp();
            String password = PaymentUtils.generatePassword(shortcode, passKey, timestamp);

            // Build JSON payload using ObjectMapper for cleaner construction
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestBody = mapper.createObjectNode()
                    .put("BusinessShortCode", shortcode)
                    .put("Password", password)
                    .put("Timestamp", timestamp)
                    .put("TransactionType", "CustomerPayBillOnline")
                    .put("Amount", order.getTotalAmount().intValue()) // Ensure integer amount
                    .put("PartyA", paymentDto.getPhoneNumber())
                    .put("PartyB", shortcode)
                    .put("PhoneNumber", paymentDto.getPhoneNumber())
                    .put("CallBackURL", callbackUrl)
                    .put("AccountReference", "Order_" + payment.getId())
                    .put("TransactionDesc", "Order Payment");

            HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

            log.info("Initiating STK Push: {}", requestBody);
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/mpesa/stkpush/v1/processrequest",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            // Parse response to extract CheckoutRequestID
            JsonNode responseNode = objectMapper.readTree(response.getBody());
            String checkoutRequestId = responseNode.path("CheckoutRequestID").asText();
            payment.setCheckoutRequestId(checkoutRequestId);
            paymentRepository.save(payment);

            log.info("STK Push response: {}", response.getBody());
            return FurnitureUtils.getResponseEntity(response.getBody(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("STK Push failed: {}", ex.getMessage());
            ex.printStackTrace();
            return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> handleCallback(String body) {
        try {
            log.info("Received callback on user side : {}", body);
            JsonNode node = objectMapper.readTree(body);
            JsonNode stkCallback = node.path("Body").path("stkCallback");

            String checkoutRequestId = stkCallback.path("CheckoutRequestID").asText();
            int resultCode = stkCallback.path("ResultCode").asInt();
            String resultDesc = stkCallback.path("ResultDesc").asText();

            Payment payment = paymentRepository.findByCheckoutRequestId(checkoutRequestId);
            if (payment == null) {
                log.error("Payment not found for CheckoutRequestID: {}", checkoutRequestId);
                return FurnitureUtils.getResponseEntity("Payment not found", HttpStatus.BAD_REQUEST);
            }

            Order order = payment.getOrder();
            String productName = order.getProduct().getName();
            Long orderId = order.getId();

            if (resultCode == 0) {
                payment.setStatus("SUCCESS");
                JsonNode callbackMetadata = stkCallback.path("CallbackMetadata").path("Item");
                for (JsonNode item : callbackMetadata) {
                    if ("MpesaReceiptNumber".equals(item.path("Name").asText())) {
                        payment.setMpesaReceiptNumber(item.path("Value").asText());
                        break;
                    }
                }

                order.setOrderStatus("CONFIRMED");
                order.setPaymentStatus("PAID");
                orderRepository.save(order);

                //Send SMS notifications to user and admins
                //User user = payment.getOrder().getUser();
                String email = jwtFilter.getCurrentUser();
                Optional<User> user = userRepository.findByEmail(email);
                log.info("User is : ", user);
                String userContact = user.get().getContact();
                if (userContact != null && !userContact.isEmpty()) {
                    sendNotification(userContact, productName, orderId, user.get().getName(),"User");
                    log.info("SMS notification sent to user contact: {} (name: {}) for orderId: {}", userContact, user.get().getName(), orderId);
                } else {
                    return FurnitureUtils.getResponseEntity("User phone number not found for orderId : {} " + orderId, HttpStatus.BAD_REQUEST);
                }



                List<User> admins = userRepository.findByRole("Admin");
                if (admins.isEmpty()) {
                    return FurnitureUtils.getResponseEntity("No admins found with role 'Admin' for orderId: {}" + orderId, HttpStatus.BAD_REQUEST);
                } else {
                    for (User admin : admins) {
                        String adminContact = admin != null ? admin.getContact() : null;
                        if (admin.getContact() != null && !admin.getContact().isEmpty()) {
                            sendNotification(adminContact, productName, orderId, user.get().getName(), "Admin");
                            log.info("SMS notification sent to admin contact: {} (name: {}) for orderId: {}", adminContact, admin.getName(), orderId);
                        } else {
                            return FurnitureUtils.getResponseEntity("Admin phone number not found for adminId : {}" + admin.getId(), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            } else {
                payment.setStatus("FAILED");
            }
            payment.setTransactionDate(LocalDateTime.now());
            paymentRepository.save(payment);

            log.info("Callback processed: ResultCode={}, ResultDesc={}", resultCode, resultDesc);
            return FurnitureUtils.getResponseEntity("Callback processed: " + resultDesc, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Callback processing failed: {}", ex.getMessage());
            ex.printStackTrace();
            return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Implemented send notification here
    private void sendNotification(String phoneNumber, String productName, Long orderId, String userName, String role) {
        try {

            //Format phone number to ensure it starts with +
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                if (!phoneNumber.startsWith("+")) {
                    if (phoneNumber.startsWith("0")) {
                        phoneNumber = phoneNumber.substring(1); //added country code
                    }
                    phoneNumber = "+254" + phoneNumber; //added country code
                    log.info("Formated phone number for SMS is : {}", phoneNumber);
                } else {
                    log.info("Phone number is null or empty, skipping SMS for orderId : {}", orderId);
                    return;
                }
            }
            AfricasTalking.initialize(africastalkingUsername, africastalkingApiKey);
            SmsService smsService = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

            String message;
            if (role.equalsIgnoreCase("User")) {
                message = String.format(
                        "Thank for purchasing %s with RaphFurniture, \n" +
                                "Our team is working hard to deliver your order within three working days, \n" +
                                "Welcome again \n" +
                                "We are together in this business",
                        productName
                );
            }  else if (role.equalsIgnoreCase("Admin")) {
                message = String.format(
                        "%s paid for the product: %s (Order ID: %d)",
                        userName,
                        productName,
                        orderId
                );
            } else {
                log.warn("Unknown role: {}, skipping SMS for orderId: {}", role, orderId);
                return;
            }
            smsService.send(message, new String[]{phoneNumber}, true);

            log.info("Sms sent to {phoneNumber} orderId : {orderId} " + phoneNumber + orderId);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Failed to send SMS to {} for orderId : {} : {} ", phoneNumber, orderId, ex.getMessage());
        }
    }
}