package com.gamecms.backend.wyd.DTO;

import com.gamecms.backend.wyd.DTO.AbstractDTO.AbstractBasicAuthDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveGuildmarkRequestDTO extends AbstractBasicAuthDTO {
    private String image;
    private String guildId;
    private String username;
}
