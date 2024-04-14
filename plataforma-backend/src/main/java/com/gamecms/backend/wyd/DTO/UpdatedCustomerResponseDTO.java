package com.gamecms.backend.wyd.DTO;
import lombok.Data;

import java.util.Date;
@Data
public class UpdatedCustomerResponseDTO {
    private String email;
    private Date birthDate;
    private String name;

}