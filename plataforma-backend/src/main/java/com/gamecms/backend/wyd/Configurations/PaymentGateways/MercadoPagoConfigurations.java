package com.gamecms.backend.wyd.Configurations.PaymentGateways;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import java.util.logging.Level;

@Component
public class MercadoPagoConfigurations {

    public static final TimeZone timeZone = TimeZone.getTimeZone("GMT");


    @PostConstruct
    private void init() {
        MercadoPagoConfig.setConnectionRequestTimeout(2000);
        MercadoPagoConfig.setSocketTimeout(2000);
        MercadoPagoConfig.setLoggingLevel(Level.FINE);
    }
}
