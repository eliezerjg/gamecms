package com.gamecms.backend.wyd.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListRequestHistoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blacklist_id")
    @JsonIgnore
    private Blacklist blackListItem;
    private String method;

    @Column(columnDefinition = "LONGTEXT")
    private String params;

    @Column(columnDefinition = "LONGTEXT")
    private String uri;
}
