package com.gamecms.backend.wyd.DTO;

import java.util.List;


public class MercadoPagoPaymentsSearchResponseDTO {

    public class Payment{
        public String date_approved;
        public String status_detail;
        public String external_reference;
    }
    public  List<Payment> results;
}
