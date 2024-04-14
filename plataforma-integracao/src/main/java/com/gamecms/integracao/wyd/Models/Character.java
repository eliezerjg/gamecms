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
@Table(name = "characteres")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="slug")
    private String slug;

    @Column(name="account_id")
    private Integer accountId;

    private String nick;

    private Integer level;

    @Column(name="class")
    private Integer className;

    @Column(name="evolution")
    private Integer evolution;

    @Column(name="kingdom")
    private Integer kingdom;

    @Column(name="guild_id")
    private Integer guild_id;

    @Column(name="guildlevel")
    private Integer guildlevel;

    @Column(name="points")
    private Integer points;

    @Column(name="frags")
    private int frags;

    @Column(name="has_subcelestial")
    private boolean hasSubcelestial;

    @Column(name="subcelestial_level")
    private Integer subCelestialLevel;

    @Column(name="celestial_level")
    private Integer celestialLevel;

    @Column(name="total_level")
    private long totalLevel;

    @Column(name="darkshadow_frag")
    private Integer darkshadowFrag;

    @Column(name="verid_frag")
    private Integer veridFrag;

    @Column(name="kefra_frag")
    private Integer kefraFrag;

    @Column(name="arena_kill")
    private Integer arenaKill;

    @Column(name="arena_win")
    private Integer arenaWin;

}