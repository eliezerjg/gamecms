package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.ShopProduct;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ShopRepository extends JpaRepository<ShopProduct, Long>, PagingAndSortingRepository<ShopProduct, Long> {
    @NotNull
    Page<ShopProduct> findAllByCustomerId(@NotNull Pageable pageable, Long customerId);

    List<ShopProduct> findAllByCustomerId(Long customerId);

    @NotNull
    Optional<ShopProduct> findByIdAndCustomerId(Long id, Long customerId);


}
