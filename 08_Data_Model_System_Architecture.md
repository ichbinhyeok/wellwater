# 문서 8. Data Model & System Architecture

## 1. 목적

이 문서는 제품을 “글 모음”이 아니라 “구조화된 decision system”으로 구현하기 위한 데이터 모델과 시스템 모듈을 정의한다.

## 2. 핵심 시스템 모듈

1. intake layer  
2. normalization layer  
3. decision engine  
4. scenario builder  
5. explanation renderer  
6. CTA router  
7. save/share/PDF layer  
8. analytics/event layer  
9. admin registry layer

## 3. 레지스트리 구조

## 3-1. Contaminant Registry
필드 예시:
- canonical_name
- aliases
- support_tier
- problem_type_default
- urgency_rules
- unit_whitelist
- threshold_refs
- sensitive_groups
- typical_symptoms
- typical_sources
- candidate_action_modes
- candidate_treatment_categories
- verification_needed
- source_version

## 3-2. Symptom Registry
- symptom_name
- aliases
- possible_causes
- related_contaminants
- emergency_flags
- recommended_tests
- whole_house_likelihood
- related_triggers
- default_cta

## 3-3. Treatment Capability Registry
- treatment_category
- scope
- claim_types
- suitable_for
- not_suitable_for
- pretreatment_needs
- maintenance_pattern
- replacement_cycle
- certification_standards_to_check
- notes

## 3-4. Cost Registry
- treatment_category
- low_range
- mid_range
- high_range
- maintenance_low
- maintenance_high
- region_variance_note
- source
- checked_at

## 3-5. State Resource Registry
- state
- certified_lab_directory
- health_department
- environmental_department
- extension_resources
- special_notes
- checked_at

## 3-6. Offer Registry
- vendor
- offer_type
- applicable_branch
- applicable_category
- disclosure_copy
- public_terms
- checked_at
- active_flag

## 3-7. Copy Block Registry
- verdict_key
- why_copy
- today_actions_copy
- later_actions_copy
- unsupported_copy
- disclosure_copy
- tone_version

## 4. 엔진 처리 흐름

### Step 1. Intake
- manual form
- symptom selector
- trigger selector
- upload placeholder

### Step 2. Normalize
- unit conversion
- qualifier parsing
- sample freshness
- source type(raw/treated)
- certified lab confidence
- support tier assignment

### Step 3. Classify
- urgency
- scope
- problem type
- action mode

### Step 4. Scenario Build
- verify-first
- drinking-only protection
- whole-house treatment
- source/plumbing investigation
- refer-out

### Step 5. Render
- verdict
- why this verdict
- timeline
- scenario compare
- cost
- sources/disclosure

### Step 6. Route CTA
- testing
- local guidance
- category compare
- local quote
- save/PDF

## 5. 버저닝 원칙

- decision rules versioned
- threshold/source registry versioned
- CTA logic versioned
- copy blocks versioned
- saved reports에 decision version 표시 가능

## 6. QA 원칙

### 자동 QA
- required fields validation
- unit mapping validation
- stale sample flag
- unsupported analyte routing
- scenario minimum count
- missing disclosure detection

### 수동 QA
- red/amber/green branch visual review
- copy tone review
- result-to-CTA coherence
- save/PDF readability
- mobile layout review

## 7. 이벤트 설계

핵심 이벤트:
- entry_mode_selected
- test_started
- test_completed
- result_viewed
- verdict_type_shown
- scenario_opened
- cta_clicked
- pdf_saved
- local_guidance_clicked
- unsupported_result_seen
- retest_recommended

## 8. 저장/공유 구조

- 개인 결과는 기본 noindex
- PDF는 상담/공유용
- share link는 만료 또는 noindex 처리
- result payload에는 decision version 포함

## 9. 접근 제어와 운영 툴

초기에는 admin UI가 단순해도 된다.  
하지만 아래는 반드시 필요하다.
- registry edit workflow
- offer activate/deactivate
- state resource update
- copy block update
- version note log

## 10. 미래 확장

- OCR/parsing
- account history
- reminder engine
- state-specific overlays
- assisted decision expansion for Tier B
- deeper compare database
