package com.gamecms.backend.wyd.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewsRequestDTO {
    String title;
    String description;
    String author;
    String tipo;
    Long id;
    Long data;
    String unformatedHtmlDescription;
}
