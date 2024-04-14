package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopProduct extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private BigDecimal value;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    private ItemCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;

    private Long donateMultiplier;

    public static List<String> getEntries() {
        Class<?> clazz = ShopProduct.class;
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        List<String> excludedItems = List.of("id", "customer");

        return Arrays.stream(fields)
                .filter(n -> !excludedItems.contains(n.getName()))
                .map(n -> n.getName()).toList();

    }
}
