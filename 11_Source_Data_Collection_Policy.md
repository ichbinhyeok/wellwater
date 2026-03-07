# 문서 11. Source Data Collection Policy

## 1. 목적

이 문서는 Well Water Verdict의 원천 데이터 수집/갱신 규칙을 정의한다.  
핵심 원칙은 하나다.

**원천 데이터는 수집/갱신 시마다 반드시 인터넷 검색을 통해 확인하고 기록한다.**

## 2. 적용 범위

아래 데이터는 모두 본 정책 대상이다.

- contaminant threshold/benchmark
- federal/state/local guidance 링크
- certification/standard claim 기준
- pSEO 페이지의 사실 근거 문장
- 수익화 링크 관련 공개 약관/조건

## 3. 수집 절차 (필수)

1. 검색
- 대상 항목마다 웹 검색을 실행한다.
- 검색 시점의 최신 문서를 우선한다.

2. 선별
- 1차 출처(공공기관, 주정부, 대학/extension, 표준기관)를 우선 선택한다.
- 상업 출처는 비용/오퍼 영역에서만 사용한다.

3. 검증
- 문서 발행일/개정일/적용일을 확인한다.
- 상충 출처가 있으면 보수적 해석 + 추가 확인 상태로 보류한다.

4. 기록
- 현재 pSEO MVP의 `data/pseo/pages.csv`에는 아래 필드를 필수 저장한다.
  - `source_url`
  - `search_query`
  - `search_performed_at` (UTC)
  - `fetched_at` (UTC)
- decision/registry CSV(`data/registry/*.csv`)에는 아래 확장 메타데이터를 저장한다.
  - `source_publisher`
  - `effective_date` (확인 가능 시 필수)
  - `source_type` (federal/state/university/standard/commercial)
  - `notes` (판단 근거 요약)

5. 승인
- health-adjacent 문장은 Trust 체크를 거친 뒤 반영한다.

## 4. 출처 우선순위

1. federal/public-health guidance
2. state health/environment/private well resources
3. extension/university resources
4. standards/certification references
5. commercial sources (cost/offer 전용)

## 5. 금지 규칙

- 검색 없이 과거 값 복붙 후 배포
- 출처 URL 없는 레코드 배포
- 발행일/갱신일 미확인 상태에서 건강 관련 강한 결론에 사용
- 상업 출처를 health 판단의 1차 근거로 사용

## 6. 신선도(SLA)

- health 관련 기준 문장: 30일 이내 재검토
- state/federal guidance 링크: 30일 이내 링크 유효성 점검
- 상업 약관 링크: 14일 이내 재검토

SLA 초과 시:
- 해당 페이지 업데이트 우선순위를 상향한다.
- 수익화 문구보다 안내 문구를 우선 노출한다.

## 7. 감사 가능성

- 모든 변경은 `who/when/what/why/source` 로그를 남긴다.
- 분기별로 source drift 리뷰를 수행한다.

## 8. 구현 체크리스트

- source 메타 필드가 CSV 스키마(헤더)에 존재하는가
- source 메타 없는 레코드를 배포 차단하는가
- 검색/검증/승인 워크플로가 운영 화면에서 강제되는가
- stale source 경보가 대시보드에서 보이는가
