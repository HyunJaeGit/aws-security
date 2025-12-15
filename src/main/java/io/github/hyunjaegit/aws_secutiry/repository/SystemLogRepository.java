package io.github.hyunjaegit.aws_secutiry.repository;

import io.github.hyunjaegit.aws_secutiry.domain.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;


// DB에 접근하기 위한 인터페이스입니다
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {


}
