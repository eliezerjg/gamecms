package com.gamecms.integracao.wyd.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private int user_id;
    private String username;
    private String password;
    private Date created_at;
    private Date updated_at;
    private Long donate;
    private int online;
    private int numerica;
    private int divina;

    @Column(name= "Pix", columnDefinition = "VARCHAR", length = 50)
    private String Pix;
}