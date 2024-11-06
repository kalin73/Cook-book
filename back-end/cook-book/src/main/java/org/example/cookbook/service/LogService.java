package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.log.LoginLogDto;
import org.example.cookbook.model.entity.LoginLogEntity;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.repository.LoginLogRepository;
import org.example.cookbook.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LoginLogRepository loginLogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public LoginLogDto[] getLoginLogByUserEmail(String email) {
        UserEntity user = this.userRepository.findUserByEmail(email).orElse(null);
        List<LoginLogEntity> loginLog;

        if (user != null) {
            loginLog = this.loginLogRepository.findByUser(user).orElse(null);

            return this.modelMapper.map(loginLog, LoginLogDto[].class);
        }

        return null;
    }
}
