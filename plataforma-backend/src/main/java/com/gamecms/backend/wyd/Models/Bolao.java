package com.gamecms.backend.wyd.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Bolao  extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private Long value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;

}
