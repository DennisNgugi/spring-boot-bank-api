package com.example.bank.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@NoArgsConstructor
@Data
@Slf4j
public class LogsManager {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(LogsManager.class);

    static final String LOGGER_FORMAT = "TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | " +
            "Sender={} | SourceSystem={} | Response={} | ResponseCode={}  | ResponseMsg={} | " +
            "ErrorDescription={}";

    public static void error(String requestId,String transactionType,String process,
                             String processDuration,String sendrID,
                             String sourceSystem,String response,
                             String responseCode,String responseMsg,String errorDescription){
        logger.error(LOGGER_FORMAT,
                requestId,transactionType,process,processDuration,sendrID,sourceSystem,
                response,responseCode,responseMsg,errorDescription
        );

    }

    public static void info(String requestId,String transactionType,String process,
                             String processDuration,String sendrID,
                             String sourceSystem,String response,
                             String responseCode,String responseMsg,String errorDescription){
        logger.info(LOGGER_FORMAT,
                requestId,transactionType,process,processDuration,sendrID,sourceSystem,
                response,responseCode,responseMsg,errorDescription
        );

    }
}
