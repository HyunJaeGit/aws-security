# 🛡️ AWS Security-Focused project  

**보안 강화 인프라 설계 및 자동화 배포 파이프라인 구축 프로젝트**

---

### Architecture

* **VPC Networking**: `10.0.0.0/16` 대역 내 Public(EC2) / Private(RDS) 서브넷 분리
* **Security Group Chaining**:
* **App-SG**: 80, 443 포트 외 모든 외부 접근 차단
* **DB-SG**: 오직 `App-SG` ID를 통해서만 3306 포트 접근을 허용하는 보안 그룹 간 상호 참조 설정


* **CI/CD Flow**: GitHub Push → GitHub Actions (Build) → Docker Hub → AWS EC2 (Auto Deploy)

---

### Engineering Points

#### 1. Modern Tech Stack & Containerization

* **Java 21 & Spring Boot 3.4**: 가상 스레드 등 최신 기능을 고려한 안정적인 API 서버 구축
* **Dockerizing**: 어플리케이션 컨테이너화를 통해 개발 환경과 운영 환경의 일관성 유지

#### 2. Infrastructure Security

* **최소 권한 원칙(PoLP)**: RDS 인바운드 규칙 소스를 특정 IP가 아닌 **EC2 보안 그룹 ID**로 특정하여 보안성 극대화
* **VPC Isolation**: 인터넷 게이트웨이(IGW) 및 라우팅 테이블 설정을 통해 명확한 외부/내부 통신 경로 확립

#### 3. CI/CD Automation

* **GitHub Actions**: 반복되는 수동 빌드/배포 과정을 완전 자동화하여 휴먼 에러 방지
* **Secret Management**: DB 패스워드, SSH 키 등 민감 정보를 **GitHub Secrets**로 격리 관리하여 보안 사고 예방

---

### Troubleshooting

**[이슈]** EC2 서버에서 RDS 연결 시 `Communications link failure` 발생
**[원인]** 1. RDS 보안 그룹 인바운드 규칙 소스(Source) 미설정으로 인한 패킷 차단
2. VPC 내 라우팅 테이블 설정 미비로 서브넷 간 통신 경로 부재

**[해결]** // 1. RDS 보안 그룹의 인바운드 소스를 EC2 보안 그룹 ID로 특정하여 접근 허용
// 2. VPC 라우팅 테이블에 IGW 경로를 명시하고 서브넷 간 통신 활성화
// 3. 보안 그룹 간 상호 참조(SG ID 기반) 설정을 통해 연결 성공

---

### Tech Stacks

* **Backend**: Java 21, Spring Boot 3.4, JPA
* **Database**: MySQL 8.0 (AWS RDS)
* **Infrastructure**: AWS (EC2, VPC, RDS), Docker, GitHub Actions

---

**이 프로젝트의 배포 과정이나 네트워크 설정에 대해 더 궁금한 점이 있으신가요?**
