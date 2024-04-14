package com.gamecms.backend.wyd.Repositories;


import com.gamecms.backend.wyd.Models.Blacklist;
import com.gamecms.backend.wyd.Models.Whitelist;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WhitelistRepository extends JpaRepository<Whitelist, Long> {
    public Optional<Whitelist> findByIp(String ip);
    public List<Whitelist> findAllByIp(String ip);
    @NotNull
    public List<Whitelist> findAll();
}
