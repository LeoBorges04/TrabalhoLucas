package com.OsGuri.TrabalhoLucas.exception.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponseDto {
    private int status;
    private String error;
    private List<String> messages;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponseDto(int status, String error, List<String> messages, String path, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.messages = messages;
        this.path = path;
        this.timestamp = timestamp;
    }
}

