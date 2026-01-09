# AWS Security-Focused CI/CD App

**"네트워크 계층 분리와 자동화 파이프라인으로 구축한 보안 중심 백엔드 인프라"**

---

### Project Architecture

본 프로젝트는 **3-Tier 아키텍처**를 기반으로 보안과 자동화에 초점을 맞추어 설계되었습니다.

* **VPC Networking**: `10.0.0.0/16` 대역 내 Public(EC2) / Private(RDS) 서브넷 분리 및 라우팅 테이블 최적화
* **Security Group Chaining (최소 권한 원칙)**:
* **APP-SG**: 외부의 8080(Spring), 22(SSH) 포트만 개방
* **DB-SG**: 오직 `APP-SG` ID를 소스(Source)로 지정하여 3306 포트 접근을 허용 (인프라 내부 통신 보안 극대화)


* **CI/CD Flow**: GitHub Push → GitHub Actions (Build) → Docker Hub → AWS EC2 (Auto Deploy)

---

### Key Engineering Points

#### 1. 인프라 설계 및 보안 (Infrastructure & Security)

* **OS Migration**: 서비스 안정성을 위해 Ubuntu에서 AWS 최적화 OS인 **Amazon Linux 2023**으로 전환 및 `dnf` 패키지 관리 적용
* **VPC 고가용성 설계**: RDS의 Multi-AZ 제약 조건을 충족하기 위해 가용 영역(2a, 2c)별 서브넷을 확장하고 **DB 서브넷 그룹** 재구성
* **비용 및 성능 최적화**: EC2와 RDS를 동일 가용 영역(2a)에 배치하여 **AZ 간 데이터 전송 비용 절감 및 네트워크 지연(Latency) 최소화**

#### 2. 컨테이너 기반 자동화 (Docker & CI/CD)

* **Dockerizing**: `amazoncorretto:17` 기반 경량화 이미지를 통해 환경 독립성 확보
* **CI/CD 파이프라인**: GitHub Actions를 활용해 코드 푸시부터 운영 서버 반영까지 전 과정 자동화
* **Secrets Management**: DB 정보, SSH 키 등 민감 정보를 **GitHub Secrets**로 격리 관리하여 보안 사고 예방

---

### 핵심 트러블슈팅 (Troubleshooting)

#### ✔️ 가용 영역(AZ) 불일치 및 네트워크 경로 부재

* **문제**: RDS 생성 시 2개 이상의 AZ 서브넷 필요 및 EC2에서 RDS 접속 타임아웃 발생
* **원인**: 서브넷 부족 및 라우팅 테이블에 인터넷 게이트웨이(IGW) 경로 누락
* **해결**: 2c 영역 서브넷 추가 생성 및 라우팅 테이블에 `0.0.0.0/0 -> IGW` 경로를 명시하여 통신로 확보

#### ✔️ 보안 그룹(SG) 기반 접근 제어 이슈

* **문제**: EC2 서버 실행 시 `Communications link failure` 발생
* **원인**: RDS 보안 그룹이 특정 IP가 아닌 유동적인 EC2의 접근을 허용하지 않음
* **해결**: RDS 보안 그룹 인바운드 규칙 소스에 **EC2 보안 그룹 ID(sg-06cde...)**를 직접 등록하여 화이트리스트 기반 보안 실현

---

### Tech Stacks

* **Backend**: Java 21, Spring Boot 3.4, Spring Data JPA
* **Database**: MySQL 8.0 (AWS RDS)
* **DevOps**: Docker, GitHub Actions, Docker Hub
* **Cloud**: AWS (VPC, EC2, RDS, IGW, Subnet Group)

---

### Project Insights

> **"인프라의 전 과정을 직접 구축하며 시스템 전체 관점의 사고 능력을 배양했습니다."**

* 단순 배포를 넘어 **네트워크 라우팅과 보안 그룹 설계**의 중요성을 체감
* `.gitignore` 및 IAM 관리 등 실무 환경의 **보안 수칙을 개발 습관화**
* 트러블슈팅 시 로그 분석을 통해 문제의 근본 원인(네트워크/권한/코드)을 파악하는 역량 강화

---
