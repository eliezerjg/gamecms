package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByIdAndCustomerId(String id, Long customer_id);
}
