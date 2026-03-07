# 문서 3. Input / Output Spec

## 1. 목적

이 문서는 제품 엔진이 받는 입력과 내보내는 출력을 명확히 정의한다.  
기획 문구가 아니라 구현·QA 기준을 제공하는 문서다.

## 2. 입력 그룹

입력은 일곱 그룹으로 나눈다.

### 그룹 A. 샘플/결과 정보
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| analyte_name | enum/string | Y | 예: nitrate, arsenic, total coliform |
| result_value | string/number | Y | 수치, positive/negative, ND 포함 |
| unit | enum | Y | ppb, mg/L, CFU 등 |
| qualifier | enum | N | none, ND, less_than, estimated, positive, negative |
| qualifier_value | number | N | `<0.005` 같은 경우의 기준값 |
| sample_date | date | Y | 샘플 채취일 |
| sample_source | enum | Y | raw well / treated tap / unknown |
| lab_certified | enum | Y | yes / no / unknown |
| lab_name | string | N | lab identifier |

### 그룹 B. 위치/상황 정보
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| state | enum | Y | 주별 guidance 연결용 |
| event_flag | multiselect | N | flood, repair, wildfire, sudden change, post-install, home purchase |
| symptom_flag | multiselect | N | smell, stains, taste, cloudy, scale, corrosion |
| existing_treatment | multiselect | N | RO, UV, softener, iron filter, carbon, sediment, unknown |

### 그룹 C. 가구/노출 정보
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| use_scope | enum | Y | drinking only / whole house / both |
| household_size | integer | N | 가구 규모 |
| infant_present | boolean | N | 영아 동거 여부 |
| pregnancy_present | boolean | N | 임신 구성원 여부 |
| immunocompromised_present | boolean | N | 면역저하 구성원 여부 |

### 그룹 D. 증상 세부 정보
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| smell_type | enum | N | rotten egg, chemical, musty, other |
| stain_type | enum | N | orange, black, blue/green, none |
| taste_type | enum | N | metallic, salty, bitter, none |
| location_scope | enum | N | one fixture / hot only / cold only / whole house |
| change_timing | enum | N | after rain, after repair, gradual, sudden, unknown |

### 그룹 E. 데이터 품질 정보
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| unit_confidence | enum | Y | high / low / unknown |
| sample_freshness | derived | Y | fresh / aging / stale |
| completeness_score | derived | Y | required field completeness |
| analyte_supported_level | derived | Y | Tier A / B / C |

### 그룹 F. 업로드 정보(후속 단계)
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| report_upload_present | boolean | N | 보고서 업로드 여부 |
| parse_status | enum | N | not_used / parsed / parse_failed |

### 그룹 G. 사용자 의도 정보
| 필드 | 타입 | 필수 | 설명 |
|---|---|---:|---|
| entry_mode | enum | Y | result-first / symptom-first / trigger-first / post-install |
| immediate_goal | enum | N | is this dangerous / what to test / what to buy / should I retest |

## 3. 입력 검증 규칙

### 3-1. unit
- analyte별 허용 unit 목록을 가진다.
- unit이 허용 목록에 없거나 unknown이면 결론 강도를 낮춘다.
- 필요 시 canonical unit으로 변환한다.

### 3-2. qualifier
- ND, `<x`, positive/negative는 수치와 분리 저장한다.
- `<x`는 단순 숫자 x로 저장하지 않는다.
- positive/negative는 microbial류와 indicator류에서 별도 핸들링한다.

### 3-3. sample freshness
- sample date가 너무 오래되면 stale sample flag를 띄운다.
- stale이면 강한 장비 추천보다 retest 쪽으로 기울인다.

### 3-4. certified lab
- unknown 또는 no이면 confidence를 낮춘다.
- Tier B/C analyte는 certified lab requirement를 더 강하게 건다.

### 3-5. treated vs raw
- treated tap 결과는 기존 장비가 결과를 바꿨을 수 있으므로 원천 해석에 주의가 필요하다.
- raw well과 treated tap이 섞이면 source diagnosis를 분리한다.

## 4. 출력 구조

출력은 다섯 레이어로 나눈다.

### 레이어 1. Primary Verdict
```text
primary_verdict_label
primary_verdict_sentence
confidence_level
support_level
```

### 레이어 2. Multi-axis Classification
```text
urgency: immediate / prompt / routine
scope: drinking-only / whole-house / both / unclear
problem_type: microbial / chemical-health / aesthetic-operational / corrosion / unsupported
action_mode: alternate water / boil / do_not_boil / retest / compare_treatment / inspect_source / contact_local_guidance
```

### 레이어 3. Explanation
```text
key_reasons[]
data_quality_notes[]
what_we_know[]
what_we_do_not_know[]
```

### 레이어 4. Scenario Compare
시나리오는 최소 2개, 최대 4개.
각 시나리오 필드:
```text
scenario_id
scenario_title
scenario_type
fit_reason
limitations
recommended_scope
claim_requirements[]
estimated_cost_band
estimated_maintenance_band
next_action
cta_type
```

### 레이어 5. Meta / Trust
```text
sources_used[]
assumptions[]
disclosure_text
saveable_summary
local_guidance_needed (bool)
```

## 5. 지원 레벨 출력

| support_level | 의미 |
|---|---|
| Tier A | full decision 가능 |
| Tier B | assisted decision. 보수적 가이드 + local guidance |
| Tier C | refer-out. 강한 결론 금지 |

## 6. confidence level 정의

| confidence | 의미 |
|---|---|
| High | 입력 품질 충분, 지원 범위 명확, 결과 최신 |
| Medium | 일부 결손 또는 uncertainty 존재 |
| Low | stale sample, unit 불명, lab 신뢰도 낮음, analyte 복합성 높음 |

confidence가 낮으면 결과 화면에서 반드시 아래가 바뀌어야 한다.
- stronger disclaimer
- retest/local guidance 상향
- product CTA 하향

## 7. CTA 타입 정의

| cta_type | 의미 |
|---|---|
| test_kit | 검사 키트 또는 lab testing |
| certified_lab | state-certified lab 찾기 |
| local_guidance | 보건부/환경부/extension 등 |
| category_compare | 특정 treatment category 비교 |
| local_quote | local installer quote |
| save_report | PDF 저장/공유 |
| refer_out | 지원 범위 외 안내 |

## 8. 결과 화면 필수 카피 슬롯

출력 렌더링 시 아래 텍스트 슬롯을 분리 관리한다.

- verdict_sentence
- why_this_verdict
- today_actions
- this_week_actions
- later_actions
- scenario_title
- scenario_limitations
- claim_check_copy
- cost_caveat
- disclosure_copy
- unsupported_copy

## 9. QA 시나리오 최소 세트

1. nitrate + infant + raw well + fresh sample  
2. total coliform positive + flood flag  
3. iron + orange stains + whole house discomfort  
4. low pH + corrosion symptom + unknown lead status  
5. PFAS + unknown lab + stale sample  
6. treated tap only + existing RO + concern persists

이 여섯 케이스는 regression QA 기본 세트로 유지한다.
