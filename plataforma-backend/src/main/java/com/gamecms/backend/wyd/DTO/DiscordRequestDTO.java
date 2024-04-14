package com.gamecms.backend.wyd.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscordRequestDTO {
    private String content;
}
