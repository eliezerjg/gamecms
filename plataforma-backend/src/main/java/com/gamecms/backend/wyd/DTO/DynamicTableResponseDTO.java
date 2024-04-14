package com.gamecms.backend.wyd.DTO;

import com.gamecms.backend.wyd.Models.Guildmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DynamicTableResponseDTO {
    List<String> cols;
    List rows;
    List entriesModel;
}
