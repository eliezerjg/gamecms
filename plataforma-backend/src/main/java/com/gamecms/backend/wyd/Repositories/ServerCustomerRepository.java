package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.ServerCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerCustomerRepository extends JpaRepository<ServerCustomer, Long> {
    Optional<ServerCustomer> findServerCustomerByEmail(String email);

}
