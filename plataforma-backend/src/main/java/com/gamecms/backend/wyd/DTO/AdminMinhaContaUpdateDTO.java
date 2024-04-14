package com.gamecms.backend.wyd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMinhaContaUpdateDTO {
    private String email;
    private String databaseName;
    private String serverIpv4Address;
    private String serverIpv6Address;
    private String sshKey;
    private String domainFrontEnd;
    private String domainIntegration;
    private String mercadoPagoAccessToken;
    private String serverFantasyName;
    private Date releaseDate;
    private boolean enableReleaseDateCounter;
    private String releaseImage;
    private String releaseMessage;
    private String launcherImage;
    private String logoImage;
    private String favIconImage;
    private String backgroundImage;
    private String homePageTitle;
    private String homePageText;
    private String footerText;
    private String discordUrl;
    private String facebookUrl;
    private String youtubeUrl;
    private String whatsappUrl;
}
