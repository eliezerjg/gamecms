package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.DTO.CreateNewsRequestDTO;
import com.gamecms.backend.wyd.DTO.DynamicTableResponseDTO;
import com.gamecms.backend.wyd.DTO.UpdateNewsRequestDTO;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Models.Noticia;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Models.NewsType;
import com.gamecms.backend.wyd.Repositories.NoticiaRepository;
import com.gamecms.backend.wyd.Exceptions.NoticiaNaoEncontradaException;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticiasServiceImpl {

    private final NoticiaRepository noticiaRepository;
    private final ServerCustomerRepository customerRepository;

    public List<Noticia> findAllNoticias(Long customerId) {
        List<Noticia> noticias = noticiaRepository.findAllByTipoAndCustomerIdOrderByDataDesc(NewsType.NEWS, customerId);

        if (noticias.isEmpty()) {
            throw new NoticiaNaoEncontradaException("Notícias não encontradas");
        }

        return noticias;
    }

    public List<Noticia> findFirst10Noticias(Long customerId) {
        List<Noticia> noticias = noticiaRepository.findFirst10ByTipoAndCustomerIdOrderByDataDesc(NewsType.NEWS, customerId);

        if (noticias.isEmpty()) {
            throw new NoticiaNaoEncontradaException("Notícias não encontradas");
        }

        return noticias;
    }


    public Noticia findNoticiaById(Long id, Long customerId) {
        return noticiaRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new NoticiaNaoEncontradaException("Notícia não encontrada com ID: " + id));
    }


    public Page<Noticia> findAllByTipo(NewsType tipo, Pageable pageable, Long customerId) {
        return noticiaRepository.findAllByTipoAndCustomerId(tipo, pageable, customerId);
    }

    public Noticia updateNoticia(Long idNoticia, Long customerId, UpdateNewsRequestDTO updatedNewsDTO) {
        Noticia noticia = noticiaRepository.findByIdAndCustomerId(idNoticia, customerId).orElseThrow(() -> new NoticiaNaoEncontradaException("Notícia não encontrada com ID: " + idNoticia));
        noticia.setDescription(updatedNewsDTO.getDescription());
        noticia.setTitle(updatedNewsDTO.getTitle());
        noticia.setData(new Date(updatedNewsDTO.getData()));
        noticia.setAuthor(updatedNewsDTO.getAuthor());
        noticia.setTipo(NewsType.valueOf(updatedNewsDTO.getTipo()));
        noticia.setUnformatedHtmlDescription(updatedNewsDTO.getUnformatedHtmlDescription());
        log.info("Update Noticia: " + idNoticia + ", customer: " + noticia.getCustomer().getEmail());
        return noticiaRepository.save(noticia);
    }

    public DynamicTableResponseDTO getDynamicNoticiasTableByCustomerId(Long customerId) {
        DynamicTableResponseDTO tableResultset = new DynamicTableResponseDTO();
        List<Noticia> rows = noticiaRepository.findAllByCustomerIdOrderByDataDesc(customerId);
        tableResultset.setRows(rows);
        tableResultset.setCols(List.of("Id", "Data", "Title", "Author", "Description", "Tipo"));
        tableResultset.setEntriesModel(Noticia.getEntries());
        return tableResultset;
    }

    public void saveNoticiaByCustomerId(Long customerId, CreateNewsRequestDTO dto) throws ParseException {
        ServerCustomer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("Customer Not Found"));
        Noticia noticia = dto.adaptToNoticia();
        noticia.setCustomer(customer);
        noticiaRepository.save(noticia);
    }

    public void deleteNoticiaByIdAndCustomerId(Long idNoticia, Long customerId) {
        Noticia target = noticiaRepository
                .findByIdAndCustomerId(idNoticia, customerId).orElseThrow(() -> new NoticiaNaoEncontradaException("Notice not found"));
        noticiaRepository.delete(target);
    }
}
