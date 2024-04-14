package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Guildmark extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String guildId;
    private String author;

    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;

    private String password;

    public static List<String> getEntries() {
        Class<?> clazz = Guildmark.class;
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

        List<String> excludedItems = List.of("customer");

        return Arrays.stream(fields)
                .filter(n -> !excludedItems.contains(n.getName()))
                .map(n -> n.getName()).toList();

    }
}
