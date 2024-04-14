package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.Preference;
import com.gamecms.backend.wyd.Models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByCustomerIdAndImportedByServerCustomerIsFalseAndPaidIsTrue(Long customerId);
    Optional<Purchase> findByCustomerIdAndReferenceInPaymentMethodEquals(Long customerId, String reference);
    List<Purchase> findAllByCustomerIdAndPaidIsTrueAndUsernameEquals(Long customerId, String username);
    List<Purchase> findAllByCustomerId(Long customerId);
    Optional<Purchase> findByIdAndCustomerId(Long id, Long customerId);
}
