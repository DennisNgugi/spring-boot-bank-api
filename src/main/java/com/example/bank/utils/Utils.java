package com.example.bank.utils;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static LocalDateTime dateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Map<String, Date> startAndEndOfDay(){
        Date date = new Date();

       LocalDateTime localDateTime = Utils.dateToLocalDateTime(date);
       LocalDateTime startDay = localDateTime.with(LocalTime.MIN);
       LocalDateTime endDay = localDateTime.with(LocalTime.MAX);

       Map<String, Date> dateValues = new HashMap<>();
       dateValues.put("startDay",Utils.localDateTimeToDate(startDay));
       dateValues.put("endDay",Utils.localDateTimeToDate(endDay));

       return dateValues;

    }

    public static Map<String,String> getLoggingParameters(String transactionType,String process,String processDuration,String httpStatus,
                                                   String responseMsg,String errorDescription){
        Map<String,String> logParams = new HashMap<>();
        logParams.put("transactionType", transactionType);
        logParams.put("process",process);
        logParams.put("processDuration",processDuration);
        logParams.put("httpStatus", httpStatus);
        logParams.put("responseMsg",responseMsg);
        logParams.put("errorDescription",errorDescription);

        return logParams;
    }
}
