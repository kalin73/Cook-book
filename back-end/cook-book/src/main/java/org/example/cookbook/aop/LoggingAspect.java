package org.example.cookbook.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.example.cookbook.model.entity.LoginLogEntity;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.repository.LoginLogRepository;
import org.example.cookbook.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final LoginLogRepository loginLogRepository;

    @After("execution(* org.example.cookbook.service.UserService.login(..))")
    public void afterLogin(JoinPoint joinPoint) {
        UserService userService = (UserService) joinPoint.getTarget();
        UserEntity user = userService.getCurrentLoggedInUser();

        if (user != null) {
            this.loginLogRepository.save(new LoginLogEntity(user, LocalDateTime.now()));
        }

    }
}
