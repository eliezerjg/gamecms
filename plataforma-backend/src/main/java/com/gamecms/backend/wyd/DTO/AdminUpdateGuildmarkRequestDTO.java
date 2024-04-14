package com.gamecms.backend.wyd.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateGuildmarkRequestDTO {
    private String image;
    private String guildId;
    private String author;
    private String password;
}
