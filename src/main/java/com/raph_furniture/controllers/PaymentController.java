package com.raph_furniture.controllers;

import com.raph_furniture.dto.PaymentDto;
import com.raph_furniture.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Add your annotations here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path = "/stkpush")
    public ResponseEntity<String> stkPush(@RequestBody PaymentDto paymentDto) {
        return paymentService.initiateStkPush(paymentDto);
    }

    @PostMapping(path = "/callback")
    public ResponseEntity<String> callback(@RequestBody String body) {
        return paymentService.handleCallback(body);
    }
}
