package org.example.cookbook.model.dto.log;

import lombok.Data;
import org.example.cookbook.model.dto.user.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class LoginLogDto {
    private UserDto user;
    private LocalDateTime date;

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return date.format(formatter);
    }
}
