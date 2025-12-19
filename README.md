# AWS Security-Focused CI/CD App

> **Java 21 / Spring Boot 3.4 / Docker / GitHub Actions / AWS**
> 
> 보안이 강화된 인프라 설계와 자동화 배포 파이프라인 구축을 목표로 한 백엔드 프로젝트입니다.

## Architecture Diagrams
- **VPC**: 10.0.0.0/16
- **Subnets**: Public (EC2) / Private (RDS) 분리
- **Security**: Security Group Chaining (App-SG → DB-SG)
- **CI/CD Flow**: GitHub Push → GitHub Actions (Build) → Docker Hub → AWS EC2 (Auto Deploy)

## Engineering
### 1. Modern Tech Stack
- **Java 21 & Spring Boot 3.4**: 최신 환경에서의 안정적인 API 서버 구축.
- **Dockerizing**: 어플리케이션을 컨테이너화하여 환경에 구애받지 않는 배포 환경 조성.

### 2. Infrastructure Security
- **Security Group Chaining**: RDS의 3306 포트를 오직 EC2 보안 그룹 ID를 통해서만 접근 가능하도록 설정하여 외부 공격 차단.
- **VPC Networking**: 인터넷 게이트웨이와 라우팅 테이블 설정을 통해 명확한 외부/내부 통신 경로 확립.

### 3. CI/CD Automation
- **GitHub Actions**: 반복되는 수동 빌드/배포 과정을 완전 자동화.
- **Secret Management**: DB 패스워드, SSH 키 등 민감 정보를 GitHub Secrets로 관리하여 보안 사고 방지.

## 🛠️ Troubleshooting (핵심 경험)
- **이슈**: EC2 서버에서 RDS 연결 시 `Communications link failure` 발생.
- **원인**: 보안 그룹 설정 미비로 인한 DB 접근 차단 및 라우팅 테이블 설정 오류.
- **해결**: RDS 보안 그룹 인바운드 규칙의 소스를 EC2 보안 그룹 ID로 특정하고, VPC 내 라우팅 테이블에 IGW 경로를 명시하여 통신 성공.
- **성과**: 인프라 간 보안 통신 원리 이해 및 네트워크 디버깅 역량 강화.

## 💻 Tech Stacks
- **Backend**: Java 21, Spring Boot 3.4, JPA
- **Database**: MySQL 8.0 (AWS RDS)
- **Infrastructure**: AWS (EC2, VPC, RDS), Docker, GitHub Actions
