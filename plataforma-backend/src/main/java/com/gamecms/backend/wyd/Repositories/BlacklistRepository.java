package com.gamecms.backend.wyd.Repositories;


import com.gamecms.backend.wyd.Models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    public Optional<Blacklist> findByIp(String ip);

}
