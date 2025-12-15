package io.github.hyunjaegit.aws_secutiry.controller;

import io.github.hyunjaegit.aws_secutiry.domain.SystemLog;
import io.github.hyunjaegit.aws_secutiry.repository.SystemLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 실제로 웹에서 호출할 수 있는 API
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final SystemLogRepository systemLogRepository;

    // 1. 로그 생성 (Create)
    @PostMapping
    public SystemLog createLog(@RequestBody String activity, HttpServletRequest request) {
        SystemLog log = SystemLog.builder()
                .activity(activity)
                .clientIp(request.getRemoteAddr())
                .build();
        return systemLogRepository.save(log);
    }

    // 2. 모든 로그 조회 (Read)
    @GetMapping
    public List<SystemLog> getAllLogs() {
        return systemLogRepository.findAll();
    }

}
