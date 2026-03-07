# 문서 2. Product PRD

## 1. 목적

사용자가 private well 관련 문제를 겪을 때, 아래 질문에 빠르게 답하도록 돕는 제품을 만든다.

- 지금 위험한가?
- 오늘 무엇을 해야 하나?
- 다시 검사해야 하나?
- drinking-water-only 대응이면 충분한가?
- whole-house treatment가 필요한가?
- local guidance나 certified lab이 먼저인가?
- 장비를 비교해도 되는 상태인가?

## 2. 타깃 사용자 상태

### 상태 A. Result-first
lab report 또는 inspection result를 갖고 있다.

### 상태 B. Symptom-first
냄새, 얼룩, 맛, 물색, 스케일, 부식 문제로 들어온다.

### 상태 C. Trigger-first
flood, repair, new home purchase, post-install, sudden change로 들어온다.

### 상태 D. Post-install verification
장비가 이미 있고, 효과 확인이나 추가 대응이 필요하다.

## 3. 핵심 사용자 흐름

## 3-1. Result-first flow
Landing → 입력 폼 → 결과 정규화 → verdict → scenario compare → CTA

성공 조건:
- 입력 완료율이 높다.
- unit/qualifier 오류를 잡아낸다.
- 결과 페이지 첫 줄이 행동 문장이다.

## 3-2. Symptom-first flow
Symptom page → triage prompt → 권장 검사/주의 사항 → testing CTA 또는 result engine

성공 조건:
- 곧바로 장비 판매로 가지 않는다.
- 가능한 원인과 검사 우선순위를 명확히 준다.

## 3-3. Trigger-first flow
Trigger page → immediate guidance → what to test now → result engine / local guidance

성공 조건:
- flood, wildfire, chemical concern에서 boil/do-not-boil 안내가 자동으로 혼동되지 않는다.

## 3-4. Post-install verification flow
Post-treatment page → retest need 판단 → maintenance/verification guidance → save/share/PDF

성공 조건:
- verification가 제품 외부가 아니라 제품 내부 루프로 남는다.

## 4. 정보 구조

홈페이지와 제품 표면은 아래 구조를 따른다.

### 홈페이지
1. Hero + 3-entry chooser
2. How it works
3. Common problems grid
4. Tool preview
5. Scenario compare sample
6. Trust block
7. Disclosure
8. FAQ

### 제품 엔진
- Symptom triage
- Testing recommender
- Result interpreter
- Treatment scenario compare
- Verification/reminder

## 5. 기능 범위

## 5-1. v1 필수 기능
- manual result entry
- symptom intake
- trigger intake
- multi-axis verdict
- scenario compare 2안 이상
- cost & upkeep block
- sources & assumptions block
- PDF save/share
- CTA routing
- noindex result pages
- analytics events

## 5-2. v1.5 기능
- state resource overlay
- assisted decision flow for Tier B
- shareable result links
- reminder/email hooks
- vendor category compare

## 5-3. 후순위 기능
- PDF/OCR lab report parsing
- account system
- city/zip scale pages
- direct appointment booking
- advanced personalization history

## 6. 결과 화면 명세

결과 화면은 반드시 아래 순서를 따른다.

### 블록 1. Primary Verdict
가장 먼저 한 문장 verdict를 보여준다.

예시:
- 지금은 drinking-water safety 대응이 우선입니다.
- 장비 구매보다 재검사와 certified lab 확인이 먼저입니다.
- 건강 위험보다 미관·운영상 문제가 중심입니다.

### 블록 2. Why This Verdict
3줄 안에서 결론의 핵심 이유를 설명한다.

### 블록 3. Action Timeline
- Today
- This week
- Later

### 블록 4. Scenario Compare
반드시 2안 이상. 가능한 템플릿:

- Verify-first
- Drinking-only protection
- Whole-house treatment
- Source/plumbing investigation
- Local guidance / refer-out

각 시나리오에는 아래 필드를 넣는다.
- 목적
- 적합한 상황
- 한계
- 범위(POU/POE/both)
- 예상 설치비
- 유지비/교체주기
- 필요한 claim
- 다음 행동

### 블록 5. Cost & Maintenance
정확한 quote가 아니라 national directional estimate로 제시한다.

### 블록 6. Sources & Assumptions
- threshold 출처 종류
- claim 확인 필요
- 비용은 지역·배관·전기·permit에 따라 변동
- 개인 결과는 교육용 decision support

### 블록 7. CTA
결과 상태에 따라 CTA 종류가 달라진다.
- test kit / certified lab
- treatment category compare
- local guidance
- local quote / lead
- save/PDF

## 7. 사용성 요구사항

- 첫 5초 안에 위험/우선행동을 알 수 있어야 한다.
- 사용자에게 장문의 원리 설명을 먼저 강요하지 않는다.
- 입력값이 부족하면 무조건 결론내리지 않는다.
- unit mismatch와 qualifier(<, ND, blank)를 친절히 처리한다.
- 결과 화면에서 explanation이 recommendation보다 먼저 온다.
- 모바일에서도 scenario compare를 읽을 수 있어야 한다.

## 8. 정책/신뢰 요구사항

- “EPA 기준 위반” 대신 “benchmark와 비교” 중심의 문장을 쓴다.
- germs / chemicals / nuisance / corrosion을 같은 톤으로 다루지 않는다.
- unsupported를 명시적으로 보여준다.
- safety action이 필요한 경우 commerce CTA를 하단으로 내린다.

## 9. 인덱싱 정책

- 결과 개별 URL: noindex
- static tool landing: index
- symptom / trigger / contaminant / compare / methodology pages: index
- shareable report URL: 기본 noindex, 필요 시 gated

## 10. 비기능 요구사항

- 모든 판정은 재현 가능해야 한다.
- 소스/threshold/claim 변경 시 버전 추적이 가능해야 한다.
- 이벤트 추적은 result start / complete / verdict / CTA까지 이어져야 한다.
- PDF 저장물은 상담/가족 공유에 쓸 수 있을 정도로 명확해야 한다.

## 11. 출시 조건

v1 출시 전 아래가 준비되어야 한다.
- Tier A analyte 지원
- result/symptom/trigger 세 개 입구
- 결과 화면 7블록 완성
- 최소 35~45개 public page 공개
- trust/methodology/disclosure 페이지 공개
- source registry 1차 구축
