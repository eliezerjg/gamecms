package com.gamecms.backend.wyd.Repositories;

import com.gamecms.backend.wyd.Models.Noticia;
import com.gamecms.backend.wyd.Models.NewsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long>, PagingAndSortingRepository<Noticia, Long> {
    Page<Noticia> findAllByTipoAndCustomerId(NewsType tipo, Pageable pageable, Long customerId);

    List<Noticia> findAllByTipoAndCustomerIdOrderByDataDesc(NewsType tipo, Long customerId);

    List<Noticia> findFirst10ByTipoAndCustomerIdOrderByDataDesc(NewsType tipo, Long customerId);

    Optional<Noticia> findByIdAndCustomerId(Long id, Long customerId);

    List<Noticia> findAllByCustomerIdOrderByDataDesc(Long customerId);
}
