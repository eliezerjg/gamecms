package com.gamecms.backend.wyd.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.util.Date;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String user;
    private String password;
    private boolean isActive;
    private String email;
    private String databaseName;
    private String serverIpv4Address;
    private String serverIpv6Address;
    private String sshKey;
    private String domainFrontEnd;
    private String domainIntegration;

    private String serverFantasyName;

    private Date releaseDate;
    private boolean enableReleaseDateCounter;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String releaseMessage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String favIconImage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String releaseImage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String launcherImage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String logoImage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String backgroundImage;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String homePageText;

    @Column(columnDefinition = "TEXT")
    private String homePageTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String footerText;

    @Column(columnDefinition = "TEXT")
    private String discordUrl;

    @Column(columnDefinition = "TEXT")
    private String facebookUrl;

    @Column(columnDefinition = "TEXT")
    private String youtubeUrl;

    @Column(columnDefinition = "TEXT")
    private String whatsappUrl;

    @Column(columnDefinition = "TEXT")
    private String discordWebhookNotifications;

}
