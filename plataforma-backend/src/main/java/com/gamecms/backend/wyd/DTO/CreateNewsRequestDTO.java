package com.gamecms.backend.wyd.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gamecms.backend.wyd.Models.Noticia;
import com.gamecms.backend.wyd.Models.NewsType;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


@NoArgsConstructor
public class CreateNewsRequestDTO {
    @JsonProperty("author")
    String author;

    @JsonProperty("data")
    String data;

    @JsonProperty("description")
    String description;

    @JsonProperty("tipo")
    String tipo;

    @JsonProperty("title")
    String title;

    @JsonProperty("unformatedHtmlDescription")
    String unformatedHtmlDescription;

    public Noticia adaptToNoticia() throws ParseException {
        NewsType tipoNoticia = NewsType.valueOf(this.tipo);

        return Noticia.builder()
                .title(this.title)
                .description(this.description)
                .author(this.author)
                .data(new SimpleDateFormat("yyyy-MM-dd").parse(this.data))
                .dateInc(Date.from(Instant.now()))
                .tipo(tipoNoticia)
                .unformatedHtmlDescription(this.unformatedHtmlDescription)
                .build();
    }
}
