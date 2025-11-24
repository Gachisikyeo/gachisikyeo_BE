# 프로젝트 이름
같이 시켜

지역 기반 공동 구매 서비스입니다.

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
