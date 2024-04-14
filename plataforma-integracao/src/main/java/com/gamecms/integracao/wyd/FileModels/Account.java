package com.gamecms.integracao.wyd.FileModels;

import com.gamecms.integracao.wyd.DTO.AbstractDTO.AbstractBasicAuthDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

    private String user;
    private String password;

    public static String getImportModel(String user, String password){
        return user + " " +  password;
    }
    public static String convertToUpperCaseOrSetEtcForSpecialChars(String initial) {
        if (initial.matches(".*[a-zA-Z].*")) {
            initial = initial.toUpperCase();
        } else {
            initial = "etc";
        }
        return initial;
    }
    public static String getPasswordFromXml(String xmlDaConta){
        return xmlDaConta.substring(xmlDaConta.indexOf("<password>") + 10, xmlDaConta.indexOf("</password>"));
    }

    public static String getRealPath(String inGamePath, String dbSrvPath, AbstractBasicAuthDTO dtoRecebido){
        return inGamePath + dbSrvPath + "/account/" + convertToUpperCaseOrSetEtcForSpecialChars(dtoRecebido.getUser().substring(0, 1)) + "/" + dtoRecebido.getUser() + ".xml";
    }

    public static String getImportPath(String inGamePath, String importUserPath, AbstractBasicAuthDTO dtoRecebido){
        return inGamePath + importUserPath + dtoRecebido.getUser() + ".txt";
    }

}
