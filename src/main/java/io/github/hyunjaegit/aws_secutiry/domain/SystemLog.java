package io.github.hyunjaegit.aws_secutiry.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemLog {

    // 데이터베이스에 저장될 표(Table)의 구조를 정의

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activity;  // 활동 내용 (예: "로그인 성공", "데이터 조회")
    private String clientIp;  // 접속한 사람의 IP
    private LocalDateTime createdAt; // 기록 시간

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}


