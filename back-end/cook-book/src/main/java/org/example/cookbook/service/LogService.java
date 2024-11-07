package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.log.LoginLogDto;
import org.example.cookbook.model.entity.LoginLogEntity;
import org.example.cookbook.repository.LoginLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LoginLogRepository loginLogRepository;
    private final ModelMapper modelMapper;

    public LoginLogDto[] getLoginLogByUserEmail(String email) {
        List<LoginLogEntity> loginLog = this.loginLogRepository.findByUserEmail(email).orElse(null);

        return loginLog != null ?
                this.modelMapper.map(loginLog, LoginLogDto[].class) :
                null;
    }

    public List<LoginLogDto> getAllLoginLogs() {
        return loginLogRepository.findAll()
                .stream()
                .map(l -> modelMapper.map(l, LoginLogDto.class))
                .toList();
    }
}
