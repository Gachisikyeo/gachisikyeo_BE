# 같이 시켜 (gachisikyeo) — Backend
---
# 프로젝트 이름
같이 시켜

**지역(법정동) 기반 공동구매 서비스**  
사용자는 거주/활동 지역을 선택하고, 상품에 열려 있는 공동구매를 탐색하거나 직접 개설하여 함께 구매할 수 있습니다.
---
## 프로젝트 한 줄 소개
- “내가 사는 동네 사람들과 같이 사고, 같이 아끼는” 지역 기반 공동구매 플랫폼의 백엔드 API 서버
---
## 핵심 기능
- **회원/인증**
  - 일반 회원가입/로그인
  - **JWT Access/Refresh** 기반 인증/인가
  - **Google OAuth2 로그인** 및 추가 가입 플로우
- **지역(법정동) 기반 서비스**
  - 시도/시군구/동 조회 및 코드(법정동) 매핑
  - 사용자/공구에 지역 정보 연결
- **상품 & 공동구매**
  - 상품 등록/조회(카테고리/인기/내 상품 등)
  - 특정 상품에 대한 **공구 목록 조회 / 공구 개설**
  - 공구 참여(Participation) 및 상태 조회
- **결제**
  - 참여건에 대한 결제 확인(Confirm) 플로우
- **파일(이미지) 업로드**
  - AWS S3 업로드/삭제 API 제공
- **판매자 기능**
  - 사업자 정보 등록
  - 판매자 대시보드(상품/매출 등 요약 조회)

---
## 배포 URL (dev profile 기준 설정값)
```text
Frontend: https://gachisikyeo.com
Backend OAuth Redirect Base: http://gachisikyeo.duckdns.org
text'''
---
## 1. 브랜치 전략

- 기본 브랜치: `main`
    - 항상 배포 가능한 상태 유지
    - **직접 `push` 금지, 오직 PR(Pull Request)으로만 변경 가능**
- 기능 개발 브랜치: `feature/*`
    - 예: `feature/login`, `feature/signup`, `feature/cart-api`
- 버그 수정 브랜치: `fix/*`
    - 예: `fix/login-error`
- 각 커밋 컨벤션을 준수하세요
    - 예: git commit -m "feature" or "refactor" 등등
---

## 2. 작업 순서 (팀원 공통 규칙)

### 2-1. 최초 한 번만 (맨 처음 셋업)

```bash
git clone <레포지토리-URL>
cd <폴더이름>
git checkout main

