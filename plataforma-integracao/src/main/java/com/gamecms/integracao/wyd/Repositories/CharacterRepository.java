package com.gamecms.integracao.wyd.Repositories;

import com.gamecms.integracao.wyd.Models.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findTop100ByOrderByFragsDesc();
    List<Character> findTop5ByOrderByFragsDesc();
}