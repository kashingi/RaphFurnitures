package com.raph_furniture.services;

import com.raph_furniture.dto.PaymentDto;
import org.springframework.http.ResponseEntity;

//Add your annotations here
public interface PaymentService {

    ResponseEntity<String> initiateStkPush(PaymentDto paymentDto);

    ResponseEntity<String> handleCallback(String body);
}
