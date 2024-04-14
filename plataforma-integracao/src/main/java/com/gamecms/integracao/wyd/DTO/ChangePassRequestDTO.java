package com.gamecms.integracao.wyd.DTO;

import com.gamecms.integracao.wyd.DTO.AbstractDTO.AbstractBasicAuthDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassRequestDTO extends AbstractBasicAuthDTO {
    @NotNull
    String newPassword;
}
