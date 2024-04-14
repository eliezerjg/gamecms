package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Preference findByExternalReferenceAndCustomerId(String externalReference, Long customerId);
    Optional<Preference> findByIdAndCustomerId(Long id, Long customerId);
    List<Preference> findAllByCustomerId(Long customerId);

    List<Preference> findAllByCustomerIdAndUsernameEqualsAndStatusDetailEquals(Long customerId, String username, String statusDetail);
    List<Preference> findAllByCustomerIdAndImportedByCustomerIsFalseAndStatusDetailEquals(Long customerId, String statusDetail);
}
