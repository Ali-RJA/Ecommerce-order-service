package com.urbanthreads.orderservice.repository;

import com.urbanthreads.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardCompanyRepository extends JpaRepository<OrderItem, Long> {
}
