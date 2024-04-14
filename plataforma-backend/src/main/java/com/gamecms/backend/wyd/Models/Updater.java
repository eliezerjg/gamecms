package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.Models.Abstract.AbstractUpdatableRow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Updater extends AbstractUpdatableRow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private int version;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private ServerCustomer customer;
}
