# 문서 12. Decision Engine MVP Playbook (CSV-first)

## 1. 목표

이 단계의 MVP는 `pSEO 사이트`가 아니라 **Decision Engine MVP**다.

- 제품 핵심: private well decision engine
- 유입 엔진: pSEO
- 수익 구조: 판정 이후 CTA(lead/affiliate)

핵심 KPI:
1. completed decision sessions
2. verdict 이후 CTA click rate
3. 월 수익(lead + affiliate)

## 2. 범위 정렬 (문서 00~10 기준)

아래는 MVP에서 **반드시 포함**한다.

1. 3입구 + 1엔진
- result-first
- symptom-first
- trigger-first
- 세 입구는 하나의 판정 엔진으로 연결

2. 판정 구조
- Tier A / Tier B / Tier C
- multi-axis verdict
  - urgency
  - scope
  - problem_type
  - action_mode
- confidence (High / Medium / Low)

3. 추천 구조
- brand-first 금지
- claim-based recommendation 필수

4. 화면/정책
- 결과 화면 7블록
- safety before commerce
- red/amber/green CTA 우선순위
- personalized result는 noindex

5. pSEO 역할
- pSEO는 유입층으로 유지
- public surface는 index
- pSEO -> 엔진 진입 CTA를 명시적으로 연결

## 3. 이번 MVP에서 제외 (의도적)

아래만 제외한다.

- DB/Flyway
- admin 운영 UI
- OCR/파일 파싱 자동화
- 계정/히스토리
- 복잡한 ingest API
- city/zip 대량 페이지

즉, **CSV를 쓰기 위해 admin/DB를 제외**하는 것이며,
decision engine 자체를 제외하는 것이 아니다.

## 4. 현재 상태 진단 (2026-03-07)

현재 레포는 아래까지 구현됨.

- pSEO public surface
- pSEO CSV 60페이지
- source/search 메타 기록
- sitemap

현재 미구현 핵심.

- 3입구 엔진 라우트
- Tier 분기
- multi-axis classifier
- claim-based scenario compare
- result 7블록(noindex)
- verdict 기반 CTA routing
- decision session analytics

## 5. MVP 아키텍처 (CSV-first)

## 5-1. Surface 분리

1. Public Index Surface (index)
- `/`
- `/well-water/family/{family}`
- `/well-water/{slug}`
- trust/methodology/static pages

2. Decision Surface (noindex)
- `/tool/result-first`
- `/tool/symptom-first`
- `/tool/trigger-first`
- `/tool/result`
- `/tool/result/{sessionId}` (share 시 만료/제한)

## 5-2. 데이터 저장 원칙

DB 없이 CSV 사용.

- `data/pseo/pages.csv`
- `data/registry/contaminant_registry.csv`
- `data/registry/symptom_registry.csv`
- `data/registry/treatment_capability_registry.csv`
- `data/registry/cost_registry.csv`
- `data/registry/state_resource_registry.csv`
- `data/registry/offer_registry.csv`
- `data/registry/copy_block_registry.csv`
- `data/registry/registry_change_audit.csv`

원천 데이터 수집은 문서 11 정책을 따른다.
- 수집/갱신 시 인터넷 검색 필수
- source metadata 필수

## 6. 결과 화면 필수 블록 (고정)

1. Primary Verdict
2. Why This Verdict
3. Action Timeline (Today / This Week / Later)
4. Scenario Compare (2~4안)
5. Cost & Maintenance (directional)
6. Sources & Assumptions
7. CTA (branch 기반)

순서 고정 원칙:
- verdict -> why -> scenario -> CTA
- safety branch에서 commerce CTA 상단 금지

## 7. 2주 실행 계획 (엔진 MVP 복구)

## Week 1

1. 엔진 코어 최소 구현
- entry DTO + validation
- Tier resolver
- multi-axis classifier (최소 규칙)
- confidence scorer

2. result-first E2E
- `/tool/result-first` 입력
- `/tool/result` 렌더
- 결과 7블록 뼈대

3. noindex 정책
- decision result 템플릿 robots=noindex,nofollow

## Week 2

1. symptom-first / trigger-first 연결
- triage -> result engine

2. CTA router
- red/amber/green 순서 강제
- claim-based copy 우선

3. 측정
- `entry_mode_selected`
- `test_completed`
- `result_viewed`
- `cta_clicked`

4. pSEO -> tool 연결 강화
- 상위 pSEO 페이지에 engine CTA 명시

## 8. 품질 게이트 (MVP 출시 전 필수)

1. 3입구 모두 결과 페이지까지 동작
2. QA 6케이스 자동 테스트 통과
3. Tier A analyte 최소 지원
4. unsupported/low-confidence 분기 노출
5. result 페이지 noindex 적용
6. 첫 상업 CTA는 verdict 아래
7. source/claim/disclosure 문구 QA 통과

## 9. 페르소나 역할 (MVP 운영)

- Owner-A (Product): 범위 고정, 출시 승인
- Owner-B (Backend): engine rule/decision 구현
- Owner-C (Frontend): JTE skeleton, 정보 위계
- Owner-D (Trust): 카피/CTA 순서 승인
- Owner-E (Data-Ops): registry/source freshness
- Owner-F (QA): 6케이스 회귀 + 정책 검증

## 10. 다음 즉시 작업 (P0)

1. 3입구 라우트 + JTE 입력 폼 생성
2. decision payload 모델 + Tier/multi-axis 최소 엔진
3. result 7블록 템플릿 + noindex
4. red/amber/green CTA router
5. analytics 이벤트 CSV 로그
6. pSEO 상세 -> tool 진입 CTA 연결
