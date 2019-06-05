package com.example.demo.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommonDateUtil {

    /**
     * LocalDate 转Date
     * @param ldate
     * @return
     */
    public static Date localDate2Date(LocalDate ldate){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = ldate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    /**
     * Date 转LocalDate
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate ldate = instant.atZone(zoneId).toLocalDate();
        return ldate;
    }

    /**
     *  String 转LocalDate
     * @param dateText  yyyyMMdd
     * @return
     */
    public static LocalDate string2LocalDate(String dateText){
        return LocalDate.parse(dateText, DateTimeFormatter.BASIC_ISO_DATE);
    }

    public static String localDate2Str(LocalDate ldate,DateTimeFormatter formatter){
        return ldate.format(formatter);
    }

    public static void main(String[] args) {
        String dateText = "20180924";
        LocalDate ldate = LocalDate.parse(dateText, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println( localDate2Date(ldate));
        System.out.println( date2LocalDate(localDate2Date(ldate)));

        System.out.println(ldate.format(DateTimeFormatter.BASIC_ISO_DATE));
    }
}
