# 문서 6. Content & SEO Architecture

## 1. 목적

이 문서는 public indexable surface를 설계한다.  
제품 엔진의 전체 범위와 SEO 공개 범위를 구분하는 것이 핵심이다.

## 2. SEO 기본 원칙

- broad review SEO를 피한다.
- problem-intent SEO를 잡는다.
- tool은 traffic page가 아니라 conversion engine이다.
- symptom / contaminant / trigger / compare 페이지가 유입 엔진이다.
- result pages는 개인화 asset이므로 기본 noindex다.

## 3. 핵심 query family

### Family A. 결과 해석형
- well water test results
- interpret well water lab report
- high nitrate well water
- positive coliform well water
- arsenic in well water what to do

### Family B. 증상형
- rotten egg smell well water
- orange stains well water
- black stains well water
- metallic taste well water
- cloudy well water
- scale buildup well water

### Family C. 비교형
- UV vs RO for well water
- softener vs iron filter
- whole-house vs under-sink RO
- test kit vs certified lab
- best treatment for high iron well water

### Family D. 사건형
- after flood well water test
- retest after treatment
- new house with well water
- well water changed taste or smell
- after well repair test

## 4. 초기 indexable surface

초기 공개는 35~45페이지를 목표로 한다.

### 4-1. Hub
- /well-water-test-results/
- /well-water-treatment-selector/

### 4-2. Contaminants
- /well-water/nitrate/
- /well-water/arsenic/
- /well-water/coliform/
- /well-water/e-coli/
- /well-water/iron/
- /well-water/manganese/
- /well-water/hardness/
- /well-water/ph/
- /well-water/tds/
- /well-water/sulfate/

### 4-3. Symptoms
- /well-water/rotten-egg-smell/
- /well-water/orange-stains/
- /well-water/black-stains/
- /well-water/metallic-taste/
- /well-water/cloudy-water/
- /well-water/scale-buildup/

### 4-4. Compare
- /well-water/uv-vs-ro/
- /well-water/softener-vs-iron-filter/
- /well-water/whole-house-vs-under-sink-ro/
- /well-water/test-kit-vs-certified-lab/
- /well-water/iron-filter-vs-softener/

### 4-5. Situations / Triggers
- /well-water/after-flood/
- /well-water/after-heavy-rain/
- /well-water/after-repair/
- /well-water/home-purchase-test/
- /well-water/retest-after-treatment/

### 4-6. Trust / Methodology
- /well-water/how-it-works/
- /well-water/methodology/
- /well-water/state-certified-labs/
- /well-water/what-we-can-and-cannot-tell-you/

## 5. 페이지 템플릿

## 5-1. Symptom page
구조:
1. 문제 정의
2. 가능한 원인
3. 언제 바로 검사해야 하는가
4. 권장 검사 항목
5. risk vs nuisance framing
6. result tool CTA
7. 관련 treatment / compare 링크

## 5-2. Contaminant page
구조:
1. 이것이 무엇인가
2. health vs nuisance 구분
3. 검사 결과가 의미하는 것
4. typical next actions
5. treatment category landscape
6. verify/retest guidance
7. tool CTA

## 5-3. Trigger page
구조:
1. 왜 이 사건이 중요한가
2. 오늘 해야 할 일
3. 무엇을 검사할 것인가
4. boil/do-not-boil ambiguity 해소
5. result tool CTA
6. local guidance CTA

## 5-4. Compare page
구조:
1. 언제 A가 맞는가
2. 언제 B가 맞는가
3. whole-house vs drinking-only
4. maintenance 차이
5. cost direction
6. claim check
7. path chooser CTA

## 5-5. Methodology/Trust page
구조:
1. benchmark vs compliance
2. supported / assisted / refer-out
3. claim-based recommendation
4. local guidance routing
5. disclosure

## 6. 내부 링크 구조

- symptom → testing → tool
- trigger → testing/local guidance → tool
- contaminant → tool / compare / treatment
- compare → tool / category pages
- methodology → tool
- 결과 페이지 → 관련 static pages + save/PDF

## 7. index / noindex 원칙

### index
- static pages
- hub pages
- compare pages
- methodology pages

### noindex
- personalized result pages
- temporary share links
- thin pagination / search results
- low-information landing variants

## 8. pSEO 확장 규칙

초기에는 무리한 대량 생성 금지.  
확장은 아래 세 축까지만 허용한다.

1. symptom × situation
2. contaminant × treatment
3. symptom × treatment

예:
- rotten-egg-smell-after-rain
- nitrate-treatment-options
- orange-stains-vs-iron-filter

확장 조건:
- 고유 query intent가 명확할 것
- static template를 넘는 실질 차이가 있을 것
- 동일한 CTA/본문만 바꾼 thin page가 아닐 것

## 9. 콘텐츠 운영 원칙

- explanation 먼저, commerce 나중
- “best” 남발 금지
- 한 페이지에서 health와 nuisance를 섞더라도 위계가 분명해야 함
- methodology page를 숨기지 말 것
- unsupported 페이지도 trust asset이 될 수 있음

## 10. 측정 항목

- page family별 sessions
- family별 CTR
- symptom → tool click
- trigger → local guidance click
- contaminant → scenario compare click
- compare page → CTA click
- methodology page 도달률
