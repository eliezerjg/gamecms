import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class ArquivoUpdateResponseDTO {
    private String nome;
    private String diretorio;
    private Long tamanho;
    private Long dataModificao;

    public ArquivoUpdateResponseDTO(String nome, String diretorio, long dataModificao, long tamanho){
        this.nome = nome;
        this.diretorio = diretorio;
        this.dataModificao = dataModificao;
        this.tamanho = tamanho;
    }
}
