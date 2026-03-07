# 문서 4. Decision Engine Spec

## 1. 목적

이 문서는 판정 엔진의 구조, tier, 핵심 규칙, confidence, refer-out 조건을 정의한다.  
카피 문구가 아니라 **판정 원리**를 담는다.

## 2. 엔진 파이프라인

엔진은 아래 순서로 동작한다.

1. 입력 정규화  
2. 지원 수준 식별(Tier A/B/C)  
3. 데이터 품질 평가  
4. 다축 분류(urgency/scope/problem/action)  
5. 시나리오 생성  
6. confidence 산정  
7. CTA 우선순위 선택  
8. explanation 생성

## 3. 지원 수준 정의

### Tier A — Full Decision
초기부터 강하게 지원하는 항목.
- total coliform
- E. coli
- nitrate
- arsenic
- iron
- manganese
- hardness
- pH
- TDS
- sulfate
- sulfur odor

### Tier B — Assisted Decision
보수적 가이드 + local/state guidance + claim/lab 안내 중심.
- lead/corrosivity
- PFAS
- radium/radon
- pesticides/herbicides
- VOCs
- wildfire/flood chemical concern
- 복합 노출 의심

### Tier C — Refer-Out
강한 결론을 내리지 않는다.
- 미지원 analyte
- unit 불명
- sample 너무 오래됨
- lab 신뢰도 불명 + high-stakes branch
- 복합 오염이 심함
- raw source와 treated outcome이 뒤섞여 해석이 불안정함

## 4. 다축 판정 체계

### 축 1. Urgency
- immediate
- prompt
- routine

### 축 2. Scope
- drinking-only
- whole-house
- both
- unclear

### 축 3. Problem Type
- microbial
- chemical-health
- aesthetic-operational
- corrosion
- unsupported

### 축 4. Action Mode
- use_alternate_water
- boil
- do_not_boil
- retest
- compare_treatment
- inspect_source
- contact_local_guidance

## 5. 핵심 판정 원칙

### 원칙 A. health와 nuisance를 같은 톤으로 다루지 않는다
microbial, chemical-health, aesthetic-operational, corrosion은 결과 화면 색/카피/CTA 우선순위가 모두 달라야 한다.

### 원칙 B. product보다 action이 먼저다
immediate 또는 low-confidence high-risk 상황에서는 treatment recommendation보다 행동 가이드가 먼저다.

### 원칙 C. brand가 아니라 claim이 먼저다
장비 카테고리 비교는 가능하지만, 모델/claim 확인 없이는 단정하지 않는다.

### 원칙 D. unsupported를 숨기지 않는다
엔진이 잘 모르는 상황에서 억지 결론을 내리지 않는다.

## 6. 규칙 클러스터

## 6-1. Rule Cluster A — Microbial / fecal signal
입력 예:
- total coliform positive
- E. coli positive
- flood/repair/sudden change 동반

기본 판정:
- urgency = immediate 또는 prompt
- problem_type = microbial
- action_mode = use_alternate_water + boil 여부 분기 + retest + inspect_source + contact_local_guidance

출력 철학:
- 첫 CTA는 장비가 아니다.
- disinfection-capable treatment는 scenario 중 하나일 수 있으나 첫 답이 아니다.
- retest와 source correction이 항상 상위에 있다.

## 6-2. Rule Cluster B — Nitrate / drinking exposure sensitive
입력 예:
- nitrate 결과 높음
- infant/pregnancy flag 존재

기본 판정:
- urgency = immediate 또는 prompt
- scope = drinking-only 우선
- problem_type = chemical-health
- action_mode = use_alternate_water + retest + compare_treatment

출력 철학:
- whole-house shopping보다 drinking-water exposure 통제가 먼저다.
- POU category comparison이 상위 시나리오가 될 수 있다.
- vulnerable household flag가 verdict 강도를 끌어올린다.

## 6-3. Rule Cluster C — Arsenic / chronic health contaminant
기본 판정:
- urgency = prompt
- scope = drinking-only 또는 both
- problem_type = chemical-health
- action_mode = compare_treatment + retest/verification

출력 철학:
- category-first, claim-first
- “RO 무조건” 같은 단정 금지
- treatment 후 verification 강조

## 6-4. Rule Cluster D — Corrosion / low pH
입력 예:
- low pH
- blue/green stain
- metallic taste
- corrosion symptom

기본 판정:
- problem_type = corrosion
- action_mode = inspect_source 또는 compare_treatment + lead/corrosivity follow-up

출력 철학:
- 단순 neutralizer 추천으로 끝내지 않는다.
- lead/corrosivity follow-up를 함께 띄운다.
- 배관/fixture/source 문제를 분리해서 보여준다.

## 6-5. Rule Cluster E — Aesthetic / operational
입력 예:
- iron / manganese 높음
- orange/black stains
- sulfur odor
- hardness / scale
- TDS / sulfate 관련 nuisance

기본 판정:
- urgency = routine 또는 prompt
- scope = whole-house 또는 both
- problem_type = aesthetic-operational
- action_mode = compare_treatment

출력 철학:
- health scare 금지
- whole-house 관점을 기본으로 두고 drinking-only와 비교한다.
- 생활 불편, 유지보수, 장비 보호 관점으로 설명한다.

## 6-6. Rule Cluster F — Trigger / event
입력 예:
- flood
- wildfire
- chemical spill concern
- new home purchase
- post-install
- sudden change

기본 판정:
- event 성격에 따라 microbial / chemical-health / unsupported로 분기
- action_mode는 inspect_source, retest, contact_local_guidance가 먼저

출력 철학:
- “끓이면 된다”를 자동으로 내보내지 않는다.
- event 이후에는 sample freshness와 local guidance가 중요하다.

## 7. Confidence 계산 논리

confidence는 아래를 바탕으로 계산한다.

가산 요소:
- Tier A analyte
- canonical unit 매칭
- fresh sample
- certified lab yes
- raw well source 명확
- symptom/event와 result가 논리적으로 일치

감산 요소:
- unit unknown
- qualifier 해석 불가
- stale sample
- certified lab no/unknown
- treated tap only
- 복합 오염 심함
- Tier B/C analyte
- event는 명확한데 결과 없음

confidence 결과는 High / Medium / Low 셋만 사용한다.

## 8. 시나리오 생성 규칙

시나리오는 항상 “하나의 정답”이 아니라 경로 비교다.

### 기본 시나리오 타입
- Verify-first
- Drinking-only protection
- Whole-house treatment
- Source/plumbing investigation
- Local guidance / refer-out

### 생성 규칙
- immediate health branch면 Verify-first 또는 Action-first 시나리오가 무조건 포함된다.
- nuisance branch면 Whole-house treatment 시나리오가 상위로 온다.
- low-confidence면 Refer-out 또는 Retest-first가 포함된다.
- 기존 장비가 있으나 문제가 지속되면 Verification 시나리오가 추가된다.

## 9. CTA 선택 규칙

### Red zone
- microbial positive
- acute drinking exposure concern
- chemical contamination event suspicion

CTA 순서:
1. alternate water / use guidance
2. boil 또는 do_not_boil
3. certified lab / local guidance
4. retest
5. 그 다음 scenario compare

### Amber zone
- stale sample
- unsupported analyte
- lab unknown
- unit mismatch
- corrosion ambiguity

CTA 순서:
1. retest
2. better lab / local guidance
3. inspect source
4. category compare는 하위

### Green zone
- nuisance/operational
- scope 명확
- confidence 높음

CTA 순서:
1. category compare
2. local quote / lead
3. save/PDF

## 10. Refer-out 조건

아래 중 하나라도 해당하면 refer-out 또는 assisted flow로 내려간다.
- support level Tier C
- low confidence + health branch
- sample age 기준 초과
- state-specific concern이 큰 analyte
- multiple unsupported analytes
- user goal은 buying인데 data가 거의 없음

## 11. 예시 decision tree

### 예시 1. Symptom-first, rotten egg smell
증상 입력 → sulfur odor 가능성 / microbial 가능성 / hot-only 여부 확인 → 권장 검사 제시 → 결과 없으면 test-first → 결과 있으면 nuisance/whole-house branch 또는 microbial branch로 연결

### 예시 2. Result-first, nitrate + infant
결과 입력 → unit 확인 → vulnerability flag 감지 → urgency 상향 → drinking-only branch → alternate water + category compare + retest

### 예시 3. Trigger-first, flood
event 입력 → immediate caution → chemical vs microbial ambiguity → do-not-assume boil → local guidance + test now + result interpreter

## 12. 운영 규칙

- 모든 rule change는 version을 남긴다.
- thresholds/claims/source 업데이트는 source registry와 함께 묶어 배포한다.
- content team은 decision rule을 임의로 바꾸지 않는다.
- 정책 리드 승인 없는 카피 변경은 health-sensitive branch에 적용하지 않는다.
