# 문서 9. Execution Roadmap & KPI

## 1. 목적

이 문서는 90일 실행안, 역할별 책임, KPI, 운영 루프를 정의한다.

## 2. 90일 실행안

## Phase 1 — 0~30일
목표: 판정 엔진 최소 가동과 trust surface 확보

할 일:
- source/threshold/claim registry 1차 구축
- unit normalization / qualifier 처리
- Tier A decision engine 구현
- result-first MVP 화면
- symptom pages 8~10개
- contaminant pages 8~10개
- trust / methodology / disclosure 페이지
- analytics 기본 이벤트 심기

성공 기준:
- result-first flow end-to-end 가능
- 3개 대표 symptom에서 tool 진입 가능
- save/PDF v1 가능
- unsupported branch 정상 동작

## Phase 2 — 31~60일
목표: 진입점 확장과 scenario compare 고도화

할 일:
- trigger-first flow 추가
- scenario compare 2안 이상 안정화
- certified lab / local guidance 연결
- CTA 이벤트 추적 정교화
- first affiliate wiring
- compare pages 4~6개 추가
- post-install / retest pages 공개

성공 기준:
- 세 입구 모두 동작
- red/amber/green branch별 CTA 우선순위 적용
- 첫 번째 상업 CTA가 verdict 아래에만 노출

## Phase 3 — 61~90일
목표: assisted flow와 운영 체계 확립

할 일:
- Tier B assisted decision 일부 추가
- state overlay logic
- equipment category compare 확장
- local quote/lead 테스트
- 성과 좋은 query family 확장
- unsupported/low-confidence 로그 분석
- 운영 대시보드 정리

성공 기준:
- assisted flow가 억지 추천 없이 작동
- monetization이 quality KPI를 해치지 않음
- 35~45개 indexable page 품질 유지

## 3. 역할별 책임

### Founder / GM
- 제품 우선순위
- 파트너십/수익화 승인
- north star KPI 관리

### PM
- 문서 간 정합성 유지
- 출시 범위 관리
- backlog 우선순위 결정

### Research / Content
- contaminant/symptom/trigger 원문 정리
- methodology 작성
- page family 운영

### Engineering
- rule engine
- registries
- result rendering
- save/PDF
- analytics

### Design
- homepage IA
- input UX
- result hierarchy
- scenario compare layout
- disclosure placement

### Trust / Policy Reviewer
- branch tone
- benchmark wording
- unsupported handling
- CTA order 점검

### Ops / BizOps
- offer registry
- cost refresh
- state resources
- partner approval tracking

## 4. KPI 체계

## 4-1. North Star
**Completed decision sessions**

정의:
- 사용자가 entry를 선택하고
- 엔진을 완료하고
- 결과를 보고
- 다음 행동을 클릭한 세션

## 4-2. Product KPI
- test_started
- test_completed
- result_viewed
- scenario_compare_opened
- pdf_saved
- certified_lab_clicked
- local_guidance_clicked

## 4-3. Revenue KPI
- kit CTA click / CVR
- equipment CTA click / CVR
- lead submit rate
- revenue per result view
- revenue per organic session

## 4-4. Quality KPI
- unsupported rate
- stale sample rate
- low-confidence rate
- retest-first recommendation share
- emergency branch usage
- red branch exit rate
- post-install verification rate

## 5. 운영 회의 리듬

### 주간
- top pages
- top verdict branches
- biggest exits
- broken flows
- new unsupported cases

### 격주
- CTA performance review
- quality vs revenue tradeoff review
- partner health review
- content gap review

### 월간
- source registry refresh
- cost/offer refresh
- state resource audit
- rule change log review

## 6. 출시 체크리스트

출시 전 아래가 모두 YES여야 한다.
- Tier A analytes 지원
- 세 입구 동작
- 결과 화면 7블록 완성
- unsupported / low-confidence 분기 존재
- disclosure placement 완료
- methodology 페이지 공개
- save/PDF QA 완료
- analytics 검증 완료
- partner links/terms 검증 완료
- noindex 정책 적용 완료

## 7. 리스크와 대응

### 리스크 1. coverage가 넓어져 모호해짐
대응: Tier A/B/C와 confidence 분리

### 리스크 2. health-adjacent 과잉확신
대응: benchmark wording, red/amber/green CTA order

### 리스크 3. 상업 정보 변동
대응: offer/cost registry refresh cadence

### 리스크 4. unsupported 입력 증가
대응: state/local guidance routing, assisted flow 확장

## 8. 최종 운영 원칙

넓게 받되, 똑같이 강하게 말하지 않는다.  
그게 이 제품의 구조적 정체성이다.
