# Well Water Verdict 문서 세트

> 현재 실행 모드: `pSEO MVP`  
> decision engine 문서는 확장 단계 참고용이며, 활성 범위는 `12_PSEO_MVP_Playbook.md` 기준.

이 문서 세트는 아래 세 가지 입력을 통합해 재구성한 실행용 패키지다.

1. 기존 기획 초안  
2. 이전 합의안(툴 퍼스트 / decision system 관점)  
3. 추가 리뷰 보드 안(3개 입구, multi-axis verdict, Tier A/B/C, claim-based recommendation, indexable surface 분리)

초기 전략의 핵심 결정은 다음 일곱 가지다.

1. 이 서비스는 정보 사이트가 아니라 **private well decision engine**이다.  
2. 진입점은 **result-first / symptom-first / trigger-first** 세 개다.  
3. 판정은 단일 라벨이 아니라 **긴급도 / 범위 / 문제유형 / 다음 행동**의 다축 구조다.  
4. 추천은 브랜드 추천이 아니라 **claim-based recommendation**이다.  
5. 지원 범위는 넓게 가져가되 결론 강도는 **Tier A / Tier B / Tier C**로 나눈다.  
6. 인덱싱 범위와 제품 범위는 분리한다. 제품 엔진은 넓고, public surface는 날카롭게 시작한다.  
7. 수익화는 항상 **판정 뒤**에 붙는다.

## 문서 읽는 순서

1. `01_Strategy_One_Pager.md`  
   방향과 포지셔닝을 잡는 문서. 대표, PM, 투자자용.

2. `02_Product_PRD.md`  
   실제 제품이 어떤 흐름과 화면으로 동작해야 하는지 정의하는 문서. PM, 디자이너, 개발자용.

3. `03_Input_Output_Spec.md`  
   입력 스키마와 결과 스키마를 구체화한 문서. PM, 개발자, QA용.

4. `04_Decision_Engine_Spec.md`  
   판정 엔진의 로직과 tier, confidence, refer-out 기준을 정의한 문서. 리서처, 엔지니어, 정책 리뷰용.

5. `05_Trust_Compliance_Guidelines.md`  
   표현 가이드, CTA 우선순위, disclosure, 안전 가드레일을 정의한 문서. 정책, 콘텐츠, 운영용.

6. `06_Content_SEO_Architecture.md`  
   인덱서블 표면, 페이지 패밀리, 내부링크, pSEO 확장 규칙을 정의한 문서. SEO, 콘텐츠, PM용.

7. `07_Monetization_Ops.md`  
   테스트 키트, 장비, 리드, 재방문 수익을 어떻게 설계할지 정리한 문서. 운영, BizOps, PM용.

8. `08_Data_Model_System_Architecture.md`  
   레지스트리, 시스템 모듈, 버저닝, QA, 저장/공유 구조를 정의한 문서. 엔지니어링용.

9. `09_Execution_Roadmap_KPI.md`  
   90일 실행안, 담당자, KPI, 주간 운영 리듬을 정의한 문서. 대표, PM, 팀리드용.

10. `10_Implementation_Plan_SpringBoot_JTE.md`  
    Spring Boot + JTE 기준 실제 구현 태스크/게이트/역할 분담 명세. PM, 엔지니어링, QA용.

11. `11_Source_Data_Collection_Policy.md`  
    원천 데이터 수집/검증/출처 기록/신선도 SLA 정책. 리서치, Trust, Ops, 엔지니어링용.

12. `12_PSEO_MVP_Playbook.md`  
    현재 활성 실행 문서. pSEO MVP 범위와 운영 루프를 정의.

## 문서 간 경계

- **전략** 문서는 “왜/무엇”만 다룬다.  
- **PRD** 문서는 “어떤 제품 경험을 제공할지”를 다룬다.  
- **I/O 스펙**은 필드와 출력 구조를 정의한다.  
- **Decision Engine**은 규칙, tier, confidence를 정의한다.  
- **Trust/Compliance**는 어떤 문장을 써도 되는지/안 되는지를 정한다.  
- **SEO/Content**는 public indexable surface를 설계한다.  
- **Monetization/Ops**는 CTA와 운영 리듬을 다룬다.  
- **Data/System**은 실제 구현과 데이터 구조를 다룬다.  
- **Roadmap/KPI**는 실행과 측정을 다룬다.

같은 내용이 여러 문서에 반복되지 않도록, 아래를 단일 소스 오브 트루스로 둔다.

- 지원 analyte 목록: `04_Decision_Engine_Spec.md`
- 입력 필드 정의: `03_Input_Output_Spec.md`
- 결과 화면 구조: `02_Product_PRD.md`
- 표현/법무/안전 가드레일: `05_Trust_Compliance_Guidelines.md`
- public URL 체계: `06_Content_SEO_Architecture.md`
- 데이터 레지스트리 구조: `08_Data_Model_System_Architecture.md`

## 권장 운영 원칙

- thresholds, claims, state resources, vendor terms는 별도 레지스트리에서 버전 관리한다.
- MVP 단계에서는 페이지 소스를 `data/pseo/pages.csv` 단일 CSV로 운영하고, 원천 데이터는 검색 증거와 함께 기록한다.
- 결과 페이지의 개인화 URL은 기본적으로 noindex다.
- unsupported와 low-confidence를 숨기지 않는다.
- health-adjacent branch에서는 product CTA보다 행동 가이드가 먼저다.
