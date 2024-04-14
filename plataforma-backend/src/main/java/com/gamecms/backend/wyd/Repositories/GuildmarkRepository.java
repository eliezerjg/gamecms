package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.Guildmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildmarkRepository extends JpaRepository<Guildmark, Long> {
    Optional<Guildmark> findByIdAndCustomerId(Long id, Long customerId);
    Optional<Guildmark> findByGuildIdAndCustomerId(String guildId, Long customerId);
    List<Guildmark> findAllByCustomerId(Long customerId);
}
