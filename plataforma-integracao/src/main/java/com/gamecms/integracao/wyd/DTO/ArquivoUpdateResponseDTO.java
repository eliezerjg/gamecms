package com.gamecms.integracao.wyd.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ArquivoUpdateResponseDTO {
    private String nome;
    private String diretorio;
    private Long tamanho;
    private Long dataModificao;

    public ArquivoUpdateResponseDTO(String nome, String diretorio, long dataModificao, Long tamanho){
        this.nome = nome;
        this.diretorio = diretorio;
        this.dataModificao = dataModificao;
        this.tamanho = tamanho;
    }
}
