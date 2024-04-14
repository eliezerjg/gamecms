package com.gamecms.backend.wyd.Repositories;


import com.gamecms.backend.wyd.Models.BlackListRequestHistoryItem;
import com.gamecms.backend.wyd.Models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlackListRequestHistoryRepository extends JpaRepository<BlackListRequestHistoryItem, Long> {
    public List<BlackListRequestHistoryItem> findAllByBlackListItem(Blacklist item);
    public BlackListRequestHistoryItem findByBlackListItem(Blacklist item);

}
