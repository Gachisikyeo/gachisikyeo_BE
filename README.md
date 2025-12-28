# 같이 시켜 (gachisikyeo) — Backend

---

## 프로젝트 이름
**같이 시켜**

## 프로젝트 소개
**지역(법정동) 기반 공동구매 서비스**  
사용자는 거주/활동 지역을 선택하고, 상품에 열려 있는 공동구매를 탐색하거나 직접 개설하여 함께 구매할 수 있습니다.

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

## 배포 URL (dev profile 기준)
```text
Frontend: https://gachisikyeo.vercel.app
Backend OAuth Redirect Base: http://gachisikyeo.duckdns.org
```
## 기술 스택

- Language: Java 17

- Framework: Spring Boot

- Security: Spring Security, JWT, OAuth2 (Google)

- Data: Spring Data JPA, MySQL

- Infra: AWS S3

- Docs: Swagger(OpenAPI)
---
## Git 협업 방식
### 브랜치 전략
```
기본 브랜치: main

항상 배포 가능한 상태 유지

직접 push 금지, 오직 PR(Pull Request)으로만 변경 가능.
제가(이승창) 검토 후, merge 하도록 하겠습니다.

PR날리시고, Postman 결과 카톡으로 보내주세요!
+ 항상 origin/main을 본인 작업 브랜치에 pull하고 작업진행!!!

기능 개발 브랜치: feature/*

예: feature/login, feature/signup, feature/cart-api

버그 수정 브랜치: fix/*

예: fix/login-error
```