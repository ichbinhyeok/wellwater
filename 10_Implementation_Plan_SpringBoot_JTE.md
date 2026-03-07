# 문서 10. Implementation Plan (Spring Boot + JTE)

> 상태: Engine MVP 구현 상세 참고 문서 (CSV-first)  
> 현재 활성 실행안은 `12_PSEO_MVP_Playbook.md`

## 1. 목적

이 문서는 현재 기획 문서(00~09)를 실제 제품으로 구현하기 위한 실행 명세서다.  
핵심 목표는 아래 3가지다.

1. `result-first / symptom-first / trigger-first` 3입구를 단일 판정 엔진으로 연결
2. 결과 화면 7블록(Primary Verdict 포함) 안정 동작
3. `safety before commerce` 원칙을 코드/화면/운영 레이어에 강제

## 2. 기술 스택 고정

- Backend: Spring Boot 4.x (현재 레포 4.0.3), Java 21
- Web: Spring WebMVC (SSR)
- Template: JTE (`jte-spring-boot-starter-4`)
- Build: Gradle
- Registry Storage (MVP): CSV 파일 기반 (`data/registry/*.csv`)
- DB/Migration: MVP 범위에서 미사용
- Cache: Caffeine (rule/registry 조회 캐시)
- Validation: Bean Validation (Jakarta)
- Testing: JUnit5 + Spring Boot Test + MockMvc
- Observability: Micrometer + structured logging

추가 운영 원칙:
- 프런트엔드는 당분간 **기능 뼈대만 구현**한다. 시각 디자인/브랜딩 고도화는 별도 디자인 스프린트에서 처리한다.
- 원천 데이터(임계치, 가이던스, 인증/표준, 비용/오퍼)는 **수집/갱신 시마다 반드시 인터넷 검색 기반으로 확인**하고 출처 메타데이터를 남긴다.

## 3. 상위 아키텍처

### 3-1. 레이어

1. `web`  
   컨트롤러, JTE 뷰 모델, noindex/header 처리, 사용자 입력 수집
2. `application`  
   유스케이스(판정 요청 처리, 시나리오 생성, CTA 라우팅)
3. `domain`  
   규칙 엔진, confidence 계산, 분류 모델, 정책 오브젝트
4. `infrastructure`  
   CSV 레지스트리 저장소, 외부 리소스(state guidance), PDF, analytics
5. `ops-admin`  
   레지스트리 편집/버전/활성화 관리

### 3-2. 패키지 구조(초안)

```text
com.example.wellwater
  ├─ web
  │   ├─ page
  │   ├─ result
  │   ├─ api
  │   └─ common
  ├─ application
  │   ├─ intake
  │   ├─ decision
  │   ├─ scenario
  │   ├─ cta
  │   └─ reporting
  ├─ domain
  │   ├─ model
  │   ├─ rules
  │   ├─ confidence
  │   └─ policy
  ├─ infrastructure
  │   ├─ persistence
  │   ├─ registry
  │   ├─ pdf
  │   └─ analytics
  └─ opsadmin
```

## 4. JTE UI 설계 기준

- 템플릿 위치: `src/main/jte`
- 공통 레이아웃:
  - `layout/main.jte` (head/meta/disclosure slot)
  - `layout/result.jte` (결과 7블록 전용)
- 페이지 그룹:
  - `pages/home.jte`
  - `pages/intake/result-first.jte`
  - `pages/intake/symptom-first.jte`
  - `pages/intake/trigger-first.jte`
  - `pages/result/view.jte`
  - `pages/static/*` (symptom/contaminant/compare/methodology)
- SEO 정책:
  - 결과 페이지: `meta robots=noindex,nofollow`
  - static page: index 허용
- 접근성:
  - 결과 타임라인/시나리오 비교는 모바일 1열 우선
  - color만으로 위험도 전달 금지(텍스트 라벨 필수)

## 5. 데이터 모델(최소 v1)

### 5-1. 핵심 CSV 파일

1. `data/registry/contaminant_registry.csv`
2. `data/registry/state_resource_registry.csv`
3. `data/registry/offer_registry.csv`
4. `data/registry/registry_change_audit.csv`

### 5-2. 저장 원칙

- 결과 스냅샷에 `decision_version`, `registry_version` 저장
- share link는 만료(`expires_at`) 필수
- 결과 원문 입력(payload)은 마스킹 룰 적용 후 저장
- registry 데이터에는 아래 출처 메타를 필수 저장:
  - `source_url`
  - `source_publisher`
  - `fetched_at`
  - `effective_date` (확인 가능한 경우)
  - `source_type` (federal/state/university/standard/commercial)

## 6. 기능 명세 백로그 (작업 단위)

아래는 실제 구현 단위(Task ID)다.  
각 Task는 PR 1개 단위까지 분할 가능하도록 작성했다.

### EPIC A. 플랫폼 기반

- `A-01` JTE 의존성/설정 추가 (starter-4)
- `A-02` 기본 레이아웃 템플릿 및 공통 컴포넌트(Disclosure, CTA 카드)
- `A-03` 프로파일 분리(`local`, `prod`) + 환경변수 템플릿
- `A-04` CSV storage 경로/권한/백업 정책 확정
- `A-05` 표준 예외 응답/검증 오류 포맷

DoD:
- 홈 페이지가 JTE로 렌더링되고, 테스트 환경에서 부팅/렌더 테스트 통과

### EPIC B. 입력/정규화

- `B-01` 입력 DTO/command 모델 구현 (`entry_mode` 포함)
- `B-02` unit whitelist/변환기 구현
- `B-03` qualifier parser (`ND`, `<x`, `positive/negative`)
- `B-04` sample freshness 계산기
- `B-05` completeness score 계산기
- `B-06` raw vs treated 해석 플래그

DoD:
- 잘못된 unit/qualifier 입력 시 친절한 오류 또는 confidence 하향이 일관 적용

### EPIC C. 판정 엔진 코어

- `C-01` support tier resolver (`Tier A/B/C`)
- `C-02` multi-axis classifier (`urgency/scope/problem_type/action_mode`)
- `C-03` Rule Cluster A (microbial)
- `C-04` Rule Cluster B (nitrate + vulnerable)
- `C-05` Rule Cluster C (arsenic)
- `C-06` Rule Cluster D (corrosion/low pH)
- `C-07` Rule Cluster E (aesthetic/operational)
- `C-08` Rule Cluster F (trigger/event)
- `C-09` confidence scorer (High/Medium/Low)
- `C-10` refer-out gate

DoD:
- 문서 3, 4의 QA 최소 6케이스 자동 테스트 통과

### EPIC D. 결과 생성/렌더링

- `D-01` 결과 payload assembler (5레이어)
- `D-02` Primary Verdict/Why/Timeline 생성기
- `D-03` scenario builder (2~4개, 타입 규칙 반영)
- `D-04` CTA router (Red/Amber/Green 우선순위)
- `D-05` 결과 JTE 페이지 7블록 렌더링
- `D-06` noindex/보안 헤더 처리

DoD:
- 결과 화면이 항상 7블록 순서를 지키고, confidence 낮을 때 disclaimer 강화

### EPIC E. 3입구 UX

- `E-01` result-first 입력 폼
- `E-02` symptom-first triage 플로우
- `E-03` trigger-first immediate guidance 플로우
- `E-04` post-install verification 플로우
- `E-05` 입력 상태 저장/복원(세션)

DoD:
- 3입구에서 모두 결과 페이지까지 E2E 경로 동작

### EPIC F. 신뢰/정책 강제

- `F-01` forbidden phrase lint(카피 검증 유틸)
- `F-02` disclosure 슬롯 강제 렌더
- `F-03` low-confidence branch에서 commerce CTA 하향 규칙
- `F-04` unsupported 분기 고정 컴포넌트
- `F-05` action-first ordering 테스트

DoD:
- red/amber branch에서 product CTA가 verdict 상단에 노출되지 않음

### EPIC G. SEO/콘텐츠

- `G-01` static page 라우팅/템플릿 기반 생성
- `G-02` symptom 8~10 페이지 제작
- `G-03` contaminant 8~10 페이지 제작
- `G-04` compare 4~6 페이지 제작
- `G-05` methodology/trust/disclosure 페이지
- `G-06` 내부링크 정책 및 sitemap

DoD:
- 최소 35~45 indexable page 구성 + 결과 페이지 noindex 검증
- 뷰 레이어는 정보 구조 중심의 skeleton 상태 유지(디자인 polish 제외)

### EPIC H. 수익화/운영

- `H-01` offer registry CRUD + active/inactive
- `H-02` cost registry 조회 및 directional estimate 렌더
- `H-03` local guidance/state resource 연결
- `H-04` CTA click tracking + branch 성능 집계
- `H-05` partner terms 검증 날짜 경고
- `H-06` 원천 데이터 수집 파이프라인(검색-검증-기록) 구현
- `H-07` source freshness SLA 점검 배치(만료/검토 필요 데이터 경보)

DoD:
- 상업 오퍼는 branch fit 조건 충족 시에만 노출
- 모든 레지스트리 레코드가 source 메타를 보유하고, 검색 없이 수기 입력된 데이터가 배포되지 않음

### EPIC I. 저장/공유/PDF

- `I-01` 결과 snapshot 저장 API
- `I-02` share link 발급/만료
- `I-03` PDF 렌더러(결과 7블록+disclosure)
- `I-04` save/report 감사 로그

DoD:
- 생성된 PDF가 상담/가족 공유용으로 읽히는 품질 확보

### EPIC J. 계측/품질/배포

- `J-01` 이벤트 택소노미 구현(`result_viewed`, `cta_clicked` 등)
- `J-02` 핵심 KPI 대시보드 쿼리
- `J-03` 회귀 테스트 세트(최소 6케이스 + E2E)
- `J-04` CI 파이프라인(테스트/정적검사)
- `J-05` 배포 체크리스트 자동 검증(noindex, disclosure, version)

DoD:
- 릴리스 후보에서 기능/품질 게이트 자동 확인 가능

## 7. 일정 계획 (12주)

### Sprint 1~2 (주 1~2): 기반 + 입력

- A-01~A-05, B-01~B-06
- 산출물: JTE 부팅, CSV 저장소 초기화, 입력검증/정규화 완료

### Sprint 3~4 (주 3~4): 엔진 코어 1차

- C-01~C-06, D-01~D-02, E-01
- 산출물: result-first 기준 Tier A 핵심 판정

### Sprint 5~6 (주 5~6): 엔진 코어 2차 + 결과화면

- C-07~C-10, D-03~D-06, E-02
- 산출물: scenario compare + CTA routing + noindex

### Sprint 7~8 (주 7~8): 3입구 완성 + 신뢰 가드레일

- E-03~E-05, F-01~F-05
- 산출물: trigger/post-install 포함, trust rules 강제

### Sprint 9~10 (주 9~10): SEO + 수익화 운영

- G-01~G-06, H-01~H-04
- 산출물: indexable surface 확장, 운영용 오퍼/비용 연결

### Sprint 11~12 (주 11~12): 저장/공유 + 출시 게이트

- H-05, I-01~I-04, J-01~J-05
- 산출물: PDF/share/대시보드/CI 포함 출시 후보

## 8. 역할 분담 (페르소나 기반)

- `Owner-A` Product Lead: 범위/우선순위/릴리스 GO-NO GO
- `Owner-B` Backend Lead: 엔진/규칙/도메인 모델
- `Owner-C` Frontend Lead: JTE 화면/UX/모바일 가독성
- `Owner-D` Trust Lead: 카피/CTA 순서/가드레일 승인
- `Owner-E` Data-Ops: registry/offer/cost/state 리프레시
- `Owner-F` QA Lead: 회귀/E2E/릴리스 체크

RACI 핵심:
- Rule 변경: B(R), D(A), A(C), F(C)
- UI 정보 위계: C(R), A(A), D(C), F(C)
- 상업 오퍼 노출: E(R), D(A), A(C)
- 배포 승인: F(R), A(A), B/C/D(Evidence)

## 9. 품질 게이트

### Gate 0 (설계 잠금)

- 입력/출력 계약 고정
- Tier A 단위/임계치/claim 테이블 잠금
- 이벤트 택소노미 잠금

### Gate 1 (엔진 최소 기능)

- result-first E2E
- QA 6케이스 자동 통과
- 결과 7블록 렌더 + noindex 적용

### Gate 2 (3입구 + 신뢰 보장)

- symptom/trigger E2E
- red/amber branch에서 action-first 검증
- unsupported 처리 100% 노출

### Gate 3 (출시 후보)

- indexable 35~45 페이지
- disclosure 위치 규칙 충족
- KPI 대시보드/경보 동작
- 파트너 terms/비용 최신성 검증

## 10. 즉시 시작할 P0 작업 (내가 바로 수행)

1. `A-01`: JTE starter-4 의존성 및 템플릿 부팅
2. `A-02`: 공통 레이아웃 + 홈/결과 페이지 뼈대
3. `B-01`: 입력 DTO와 검증 규칙 골격
4. `C-01`: support tier resolver 최소 구현
5. `D-01`: 결과 payload 스키마 클래스 정의
6. `J-03`: QA 최소 6케이스 테스트 뼈대 추가

위 6개 완료 시점이 첫 개발 마일스톤(M1)이다.
