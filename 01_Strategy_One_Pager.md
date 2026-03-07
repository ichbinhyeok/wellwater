# 문서 1. Strategy One-Pager

## 1. 제품 한 줄 정의

**Well Water Verdict**는 private well 사용자가 검사 결과, 물 이상 증상, 또는 특정 사건 정보를 입력하면, 그 상황을 건강 위험 / 미관·운영상 문제 / 부식·소스 이슈 관점에서 해석하고, 지금 무엇을 해야 하는지, 어떤 처리 범위가 맞는지, 대략 얼마가 드는지를 알려주는 웹 기반 decision engine이다.

## 2. 이 제품이 아닌 것

이 제품은 다음이 아니다.

- “best water filter” 리뷰 사이트
- 특정 장비 브랜드 추천 사이트
- 블로그형 정보 허브
- 공포 마케팅형 수질 콘텐츠 사이트

핵심은 **검사 결과와 증상을 행동으로 바꾸는 것**이다.

## 3. 문제 정의

private well 사용자는 보통 아래 네 가지 중 하나의 상태로 들어온다.

1. **결과 보유형**  
   lab report는 있는데 숫자의 의미를 모른다.

2. **증상형**  
   rotten egg smell, orange stains, black stains, metallic taste, cloudy water, scale, corrosion 같은 문제를 겪는다.

3. **사건형**  
   flood, repair, new-home purchase, post-install, taste/smell change, vulnerable household 변화가 생겼다.

4. **사후 불안형**  
   장비가 있는데도 여전히 불안하고, 다시 검사해야 하는지 모르겠다.

이들이 공통으로 겪는 진짜 문제는 “지식 부족”이 아니라 **행동 순서의 혼란**이다.

## 4. 제품 테제

이 제품이 성립하는 이유는 네 가지다.

### 4-1. 문제는 실재하고 반복적이다
private well 사용자는 주기적 검사, 사건 후 검사, 증상 대응, treatment 확인을 반복해야 한다.

### 4-2. 공공기관도 이미 도구 형태를 쓰고 있다
well-water interpretation tool이 존재한다는 것은, 이 문제가 블로그보다 도구에 더 잘 맞는다는 뜻이다.

### 4-3. 검색 의도가 구체적이다
broad “water filter”가 아니라, smell/stain/result/what-to-do형 쿼리가 많다. 이는 decision engine과 잘 맞는다.

### 4-4. 수익화 퍼널이 길다
testing → treatment → lead → verification으로 이어지는 다층 퍼널을 설계할 수 있다.

## 5. 핵심 전략 결정

### 결정 1. 입구는 세 개, 엔진은 하나
- result-first
- symptom-first
- trigger-first

세 입구 모두 하나의 판정 엔진으로 연결한다.

### 결정 2. MVP는 넓게 받되, 결론 강도를 나눈다
- Tier A: full decision
- Tier B: assisted decision
- Tier C: refer-out

### 결정 3. 추천은 claim-based다
브랜드 추천보다 “어떤 claim이 필요한가”를 먼저 말한다.

### 결정 4. public surface는 작게 시작한다
제품 범위는 넓어도, 인덱서블 URL은 high-signal page family부터 공개한다.

### 결정 5. 돈은 판정 뒤에 붙인다
verdict → why → scenario → CTA 순서가 무너지면 신뢰도와 전환율이 동시에 떨어진다.

## 6. 포지셔닝 문장

외부용 포지셔닝은 아래 문장을 기준으로 한다.

**Understand your well water. Choose the next step.**

보조 설명 문구:

- Interpret your well test. Choose the right treatment.
- From smells and stains to lab results and treatment options.
- A decision engine for private well owners.

## 7. 제품 원칙

1. tool-first  
2. symptom-aware  
3. conservative by default  
4. safety before commerce  
5. explain before recommend  
6. claim-based, not brand-first  
7. verify after treatment  
8. unsupported를 숨기지 않음  
9. local/state guidance routing 포함  
10. 개인화 결과는 index 자산이 아니라 결과 자산으로 취급

## 8. 성공 조건

이 제품이 성공했다고 볼 수 있으려면 아래 조건을 충족해야 한다.

- 사용자가 5초 안에 “지금 위험한가 / 뭘 먼저 해야 하나”를 이해한다.
- 입력값이 부족하면 억지 결론이 아니라 low-confidence / refer-out로 떨어진다.
- symptom 유입이 testing 또는 result interpretation으로 자연스럽게 이어진다.
- result 유입이 treatment compare 또는 local guidance로 자연스럽게 이어진다.
- health-sensitive 상황에서 affiliate 카드가 앞서지 않는다.
- 장비 설치 후 verification/retest loop가 작동한다.

## 9. 비목표

초기 단계에서 아래는 핵심 목표가 아니다.

- 전국 city/zip 대량 페이지
- full OCR 기반 lab report 자동 파싱
- 모든 contaminant를 강하게 다루는 encyclopedia
- 장비 리뷰 중심 콘텐츠
- 사용자 리뷰 커뮤니티
- local contractor 직접 영업 조직

## 10. 최종 한 줄 결론

이 프로젝트는 **well water 관련 정보 사이트**가 아니라,  
**private well 사용자의 검사 결과와 증상을 다음 행동으로 바꿔주는 evidence-backed decision engine**으로 정의해야 한다.
