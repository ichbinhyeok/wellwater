# 문서 12. pSEO MVP Playbook

## 1. 목표

목표는 `well water pSEO` 트래픽을 만들고 수익화 루프를 검증하는 것이다.  
현재 단계의 핵심 KPI는 아래 3개다.

1. indexable 페이지 수
2. 유입 페이지당 CTA 클릭률
3. 월 수익(광고+제휴 합산)

## 2. 이번에 살린 것

기존 문서에서 아래만 MVP에 남긴다.

- `06_Content_SEO_Architecture.md`의 page family 구조
- `07_Monetization_Ops.md`의 disclosure 및 CTA 원칙
- `05_Trust_Compliance_Guidelines.md`의 과잉확신 금지 문구 원칙
- `09_Execution_Roadmap_KPI.md`의 측정 중심 운영 리듬

## 3. 이번에 뺀 것

MVP에서는 아래를 제외한다.

- decision engine runtime 로직
- multi-axis verdict 계산
- DB/Flyway
- admin 운영 도구
- ingest API 복잡도

## 4. 현재 기술 구조

- 렌더링: Spring Boot + JTE SSR
- 데이터 소스: `data/pseo/pages.csv`
- 라우팅:
  - `/`
  - `/well-water/family/{family}`
  - `/well-water/{slug}`
  - `/sitemap.xml`

## 5. CSV 스키마

파일: `data/pseo/pages.csv`

필수 컬럼:
- `family` (`contaminants|symptoms|compares|triggers`)
- `slug`
- `title`
- `h1`
- `meta_description`
- `intro`
- `action_now`
- `what_to_test`
- `primary_cta_label`
- `primary_cta_url`
- `money_cta_label`
- `money_cta_url`
- `disclosure`
- `source_url`
- `search_query`
- `search_performed_at`
- `fetched_at`

## 6. 원천 데이터 수집 규칙

원천 데이터는 수집 시마다 반드시 인터넷 검색으로 확인한다.

운영 체크:
1. `source_url` 비어 있으면 배포 금지
2. `search_query` 비어 있으면 배포 금지
3. `search_performed_at` 비어 있으면 배포 금지

## 7. 2주 실행 사이클

주 1:
1. 키워드/페이지 추가 10개
2. 내부링크 보강
3. CTA 문구 A/B 1개

주 2:
1. GSC 노출/클릭 상위 20페이지 개선
2. 수익 전환 상위 10페이지 CTA 위치 개선
3. 저성과 페이지 10개 리라이트

## 8. 다음 우선순위

1. `pages.csv`를 26개에서 60개까지 확장
2. family별 인트로 텍스트를 intent별로 세분화
3. sitemap 제출/인덱싱 점검 자동 체크리스트 추가

