package org.example.cookbook.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
@Data
@NoArgsConstructor
public class LoginLogEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private LocalDateTime date;

    public LoginLogEntity(UserEntity user, LocalDateTime date) {
        this.user = user;
        this.date = date;
    }
}
