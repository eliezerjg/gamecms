package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.PaymentMethod;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByCustomerAndNameEquals(ServerCustomer customerId, String name);
    List<PaymentMethod> findAllByCustomer(ServerCustomer customer);
}
