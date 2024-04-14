package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.Updater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdaterRepository extends JpaRepository<Updater, Long> {
    Updater findFirstByIdNotNull();
}
