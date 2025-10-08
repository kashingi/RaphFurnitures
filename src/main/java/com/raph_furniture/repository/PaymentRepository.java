package com.raph_furniture.repository;

import com.raph_furniture.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Add your annotations here
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByCheckoutRequestId(String checkoutRequestId);
}
