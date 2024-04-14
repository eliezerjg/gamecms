package com.gamecms.integracao.wyd.FileModels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JacksonXmlRootElement(localName = "account")
public class AccountXML {
    @JacksonXmlProperty(localName = "username")
    private String username;

    @JacksonXmlProperty(localName = "password")
    private String password;

    private int cash;

    @JacksonXmlProperty(localName = "alreadyReseted")
    private int alreadyReseted;

    private Ban ban;

    private String token;

    private String tokenblock;

    private int access;

    private Storage storage;

    private int insignia;

    private int charslot;

    private Position position;

    private Water water;

    private DateTime divina;

    private DateTime sephira;



    public static class Ban {
        @JacksonXmlProperty(localName = "type")
        private int type;
        @JacksonXmlProperty(localName = "date")
        private Date date;

    }

    public static class Date {
        @JacksonXmlProperty(localName = "day")
        private int day;
        @JacksonXmlProperty(localName = "month")
        private int month;
        @JacksonXmlProperty(localName = "year")
        private int year;
        @JacksonXmlProperty(localName = "hour")
        private int hour;
        @JacksonXmlProperty(localName = "minute")
        private int minute;

        @JacksonXmlProperty(localName = "second")
        private int second;
    }

    public static class Storage {
        @JacksonXmlProperty(localName = "gold")
        private int gold;

        @JacksonXmlProperty(localName = "items")
        private List<String> items;

    }

    public static class Position {
        @JacksonXmlProperty(isAttribute = true)
        private int x;

        @JacksonXmlProperty(isAttribute = true)
        private int y;

    }

    public static class Water {
        @JacksonXmlProperty(localName = "day")
        private int day;
        @JacksonXmlProperty(localName = "total")
        private int total;

    }

    public static class DateTime {
        @JacksonXmlProperty(localName = "day")
        private int day;

        @JacksonXmlProperty(localName = "month")
        private int month;

        @JacksonXmlProperty(localName = "year")
        private int year;

        @JacksonXmlProperty(localName = "hour")
        private int hour;

        @JacksonXmlProperty(localName = "minute")
        private int minute;

        @JacksonXmlProperty(localName = "second")
        private int second;

    }




    public static class Affects {
        @JacksonXmlProperty(localName = "affect")
        private List<Affect> affectList;
    }

    public static class Affect {
        @JacksonXmlProperty(isAttribute = true, localName = "skillIndex")
        private int skillIndex;

        @JacksonXmlProperty(isAttribute = true, localName = "index")
        private int index;

        @JacksonXmlProperty(isAttribute = true, localName = "master")
        private int master;

        @JacksonXmlProperty(isAttribute = true, localName = "value")
        private int value;

        @JacksonXmlProperty(isAttribute = true, localName = "time")
        private int time;

    }


    public static class SkillBar2 {
        @JacksonXmlProperty(localName = "sk")
        private List<Skill> skillList;

    }

    public static class Skill {
        @JacksonXmlProperty(isAttribute = true, localName = "index")
        private int index;

        private int value;

    }


    public static class Nightmares {
        @JacksonXmlProperty(localName = "nightmare")
        private List<Nightmare> nightmareList;

    }

    public static class Nightmare {
        @JacksonXmlProperty(isAttribute = true, localName = "index")
        private int index;

        @JacksonXmlProperty(isAttribute = true, localName = "x")
        private int x;

        @JacksonXmlProperty(isAttribute = true, localName = "y")
        private int y;

    }

}