package io.github.hyunjaegit.aws_secutiry.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 데이터베이스에 저장될 표(Table)의 구조를 정의
@Entity // 이 클래스가 데이터베이스의 테이블과 1:1로 매칭되는 객체임을 선언합니다.
@Getter
@NoArgsConstructor // JPA는 기본 생성자가 반드시 필요하므로 추가합니다.
@AllArgsConstructor
@Builder
public class SystemLog {

    @Id // 이 필드가 테이블의 'Primary Key(기본키)'임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호를 1, 2, 3... 순으로 DB가 알아서 올리도록 설정합니다.
    private Long id;

    private String activity;  // 어떤 활동이 일어났는지 기록합니다.
    private String clientIp;  // 보안을 위해 접속자의 IP 주소를 저장합니다.
    private LocalDateTime createdAt; // 이벤트가 발생한 정확한 시간을 기록합니다.

    @PrePersist // 데이터가 DB에 저장(Insert)되기 직전에 이 메소드를 실행하라는 뜻입니다.
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // 저장되는 순간의 시간을 자동으로 채워줍니다.
    }
}




