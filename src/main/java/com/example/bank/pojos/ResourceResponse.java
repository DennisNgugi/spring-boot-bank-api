package com.example.bank.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
public class ResourceResponse {

    private String message;
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private int status;
    private Object body;

    public ResourceResponse(String message, int status, Object body) {
        this.message = message;
        this.status = status;
        this.body = body;
    }
}
