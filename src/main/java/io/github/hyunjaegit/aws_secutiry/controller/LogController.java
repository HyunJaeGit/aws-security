package io.github.hyunjaegit.aws_secutiry.controller;

import io.github.hyunjaegit.aws_secutiry.domain.SystemLog;
import io.github.hyunjaegit.aws_secutiry.repository.SystemLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 실제로 웹에서 호출할 수 있는 API
@RestController // JSON 형태로 데이터를 주고받는 API 컨트롤러임을 선언합니다.
@RequestMapping("/api/logs") // 이 컨트롤러의 모든 주소는 /api/logs로 시작합니다.
@RequiredArgsConstructor // final이 붙은 필드(repository)를 스프링이 알아서 주입해줍니다.
public class LogController {

    private final SystemLogRepository systemLogRepository;

    /**
     * 로그 생성 (Create)
     * POST 방식으로 /api/logs 요청이 오면 실행됩니다.
     */
    @PostMapping
    public SystemLog createLog(@RequestBody String activity, HttpServletRequest request) {
        // 사용자가 보낸 활동 내용과 요청자의 IP 정보를 합쳐서 객체를 만듭니다.
        SystemLog log = SystemLog.builder()
                .activity(activity)
                .clientIp(request.getRemoteAddr()) // 보안상 중요한 '누가 접속했는가'를 추출합니다.
                .build();

        // DB에 저장하고 저장된 결과를 다시 보여줍니다.
        return systemLogRepository.save(log);
    }

    /**
     * 모든 로그 조회 (Read)
     * GET 방식으로 /api/logs 요청이 오면 실행됩니다.
     */
    @GetMapping
    public List<SystemLog> getAllLogs() {
        // DB에 저장된 모든 로그 데이터를 리스트 형태로 반환합니다.
        return systemLogRepository.findAll();
    }
}
