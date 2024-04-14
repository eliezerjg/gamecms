package com.gamecms.backend.wyd.Repositories;


import com.gamecms.backend.wyd.Models.WhiteListRequestHistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListRequestHistoryRepository extends JpaRepository<WhiteListRequestHistoryItem, Long> {

}
