package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Noticia extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date data;
    private String title;
    private String author;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String unformatedHtmlDescription;


    private NewsType tipo;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;

    public static List<String> getEntries() {
        Class<?> clazz = Noticia.class;
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

        List<String> excludedItems = List.of("customer");

        return Arrays.stream(fields)
                .filter(n -> !excludedItems.contains(n.getName()))
                .map(n -> n.getName()).toList();

    }
}
