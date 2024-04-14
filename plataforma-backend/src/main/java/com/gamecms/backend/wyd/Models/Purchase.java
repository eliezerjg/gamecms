package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Purchase extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    String referenceInPaymentMethod;

    @Column(columnDefinition = "boolean default false")
    boolean paid;

    BigDecimal value;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    String webShopReferenceCart;

    @Column(columnDefinition = "boolean default false")
    private boolean importedByServerCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id")
    @JsonIgnore
    private PaymentMethod method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;

    public static List<String> getEntries() {
        Class<?> clazz = Purchase.class;
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

        List<String> excludedItems = List.of("customer", "referenceInPaymentMethod", "method");

        return Arrays.stream(fields)
                .filter(n -> !excludedItems.contains(n.getName()))
                .map(n -> n.getName()).toList();

    }


}
