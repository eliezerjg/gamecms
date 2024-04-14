package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Preference extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String mercadoPagoPreferenceId;

    String externalReference;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    String mercadoPagoReferenceCart;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    String webShopReferenceCart;

    String username;
    BigDecimal value;
    boolean paid;

    @Column(columnDefinition = "boolean default false")
    boolean importedByCustomer;
    String statusDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;

    public static List<String> getEntries() {
        Class<?> clazz = Preference.class;
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

        List<String> excludedItems = List.of("customer", "referenceCart", "externalReference");

        return Arrays.stream(fields)
                .filter(n -> !excludedItems.contains(n.getName()))
                .map(n -> n.getName()).toList();

    }

}
