# 이어서 작업 가이드 - 2026-03-07

## 이 문서의 목적

이 레포는 지금 내 평소 로컬이 아닌 임시 PC에서 작업 중이었다.
집에 가서 바로 이어서 작업할 수 있도록 현재 상태를 한국어로 정리한 문서다.

가장 중요한 원칙은 이거다.

- 이 문서 하나만 읽으면 어디서 멈췄는지 알 수 있어야 한다.
- 다음 Codex 세션에서 바로 이어서 작업할 수 있어야 한다.
- 검증은 항상 `8081`에서 하고, 끝나면 `8081`은 내리고 기본 상태는 `8080`으로 둔다.

## 지금 우리가 하던 일

이 프로젝트에서 하던 일은 크게 세 줄기였다.

1. `pSEO -> intake -> result` 흐름을 실제 제품처럼 다듬기
2. decision engine을 `signal router` 수준에서 `threshold-aware engine` 쪽으로 올리기
3. 공개 표면과 결과 화면을 “대충 만든 프로토타입”이 아니라 “실제 제품”처럼 보이게 만들기

즉, 단순히 페이지 몇 개를 꾸민 게 아니라 아래를 동시에 올리고 있었다.

- 입력 계약
- 엔진 정밀도
- 결과 신뢰감
- 공개 표면 디자인
- 테스트 및 브라우저 검증

## 이미 끝난 것

### 1. 공개 표면 리디자인

아래 파일들은 이미 상당 부분 새 디자인으로 교체됐다.

- `src/main/jte/layout/main.jte`
- `src/main/jte/pages/home.jte`
- `src/main/jte/pages/pseo/list.jte`
- `src/main/jte/pages/pseo/detail.jte`
- `src/main/jte/pages/not-found.jte`

이 작업의 목적은 홈과 pSEO 표면을 내부 허브처럼 보이지 않게 하고,
실제 제품 랜딩처럼 보이게 만드는 것이었다.

### 2. 입력 모델 확장

아래 파일 기준으로 richer context 입력이 이미 들어갔다.

- `src/main/java/com/example/wellwater/web/tool/ToolRequest.java`
- `src/main/java/com/example/wellwater/decision/model/DecisionInput.java`

추가된 입력 범위:

- `householdSize`
- `labName`
- `locationScope`
- `changeTiming`
- `smellType`
- `stainType`
- `tasteType`
- `existingTreatments` 멀티셀렉트

### 3. 엔진 업그레이드

아래 파일들에 threshold-aware 쪽 업그레이드가 들어갔다.

- `src/main/java/com/example/wellwater/decision/normalize/DecisionInputNormalizationService.java`
- `src/main/java/com/example/wellwater/decision/DecisionEngineService.java`
- `src/main/java/com/example/wellwater/decision/model/DecisionResult.java`
- `data/registry/contaminant_registry.csv`
- `src/main/java/com/example/wellwater/decision/registry/DecisionRegistryService.java`
- `src/main/java/com/example/wellwater/decision/registry/RuleSignal.java`

이미 들어간 핵심:

- canonical unit 처리
- threshold refs 처리
- decision version / source version metadata
- secondary context로 symptom/trigger 보강
- threshold hit 시 `chemical-health`가 green compare 쪽으로 바로 빠지지 않게 라우팅 조정

### 4. outbound redirect 하드닝

아래 파일에서 리다이렉트 정책을 더 빡세게 바꿨다.

- `src/main/java/com/example/wellwater/web/tool/ToolController.java`
- `src/main/java/com/example/wellwater/decision/registry/StateResourceRegistryService.java`

방향은 임의 외부 URL 허용이 아니라 allowlist 스타일로 옮겨가는 쪽이다.

### 5. 테스트 보강

아래 테스트 파일들은 이미 추가되었거나 강화되었다.

- `src/test/java/com/example/wellwater/decision/DecisionEngineServiceTest.java`
- `src/test/java/com/example/wellwater/decision/DecisionEngineRegressionQaTest.java`
- `src/test/java/com/example/wellwater/web/tool/ToolControllerTest.java`
- `src/test/java/com/example/wellwater/web/tool/ToolRequestTest.java`
- `src/test/java/com/example/wellwater/web/RenderingSmokeTest.java`
- `src/test/java/com/example/wellwater/decision/DecisionInputNormalizationServiceTest.java`

## 현재 정확히 멈춘 지점

이번 세션 마지막에는 아래 두 작업을 동시에 밀고 있었다.

1. engine/intake/result 디자인 정리
2. threshold rule coverage 확대

여기서 1번은 `중간`, 2번은 `절반 이상`까지 간 상태다.

### 이미 적용된 것

- `src/main/jte/layout/engine.jte`는 새 visual system으로 이미 바뀌었다.
- `DecisionInputNormalizationService`는 다중 threshold ref를 모으는 쪽으로 이미 올라갔다.
- `DecisionEngineService`는 corrosion/chemical-health threshold hit가 amber 쪽으로 가도록 일부 조정되어 있다.

### 아직 남아 있는 것

아래 4개 템플릿이 아직 완전히 정리되지 않았다.

- `src/main/jte/pages/intake/result-first.jte`
- `src/main/jte/pages/intake/symptom-first.jte`
- `src/main/jte/pages/intake/trigger-first.jte`
- `src/main/jte/pages/result/view.jte`

이 파일들은 로직은 살아 있지만, 일부 문자열이 깨져 있거나 mojibake가 남아 있다.

대표적으로:

- intake 제목/설명 문구
- 결과 페이지 back button 라벨
- 결과 페이지 섹션 헤딩 일부
- scenario compare 라벨
- data quality 라벨
- link 라벨
- CTA 문구

즉, 지금 가장 큰 남은 문제는 “기능 부재”가 아니라 “engine-side UI 신뢰감 정리”다.

## 다음에 해야 할 일

### 우선순위 1. intake/result 템플릿 정리

수정 대상:

- `src/main/jte/pages/intake/result-first.jte`
- `src/main/jte/pages/intake/symptom-first.jte`
- `src/main/jte/pages/intake/trigger-first.jte`
- `src/main/jte/pages/result/view.jte`

해야 할 일:

- 깨진 문자열을 전부 정상 영어로 교체
- 새 `engine.jte` 스타일과 톤을 맞춤
- 결과 페이지를 소비자-facing 톤으로 유지
- debug 느낌 나는 표현은 줄이고, 사람이 바로 이해할 수 있는 라벨로 교체

권장 문구 예시:

- `Start with a lab result`
- `Start with a symptom`
- `Start with a recent trigger`
- `Decision result`
- `Enter new data`
- `Scenario compare`
- `Sample freshness`
- `Completeness`
- `State guidance`
- `Certified lab finder`
- `Need more help?`

### 우선순위 2. threshold coverage 테스트 추가

수정 대상:

- `src/test/java/com/example/wellwater/decision/DecisionInputNormalizationServiceTest.java`
- `src/test/java/com/example/wellwater/decision/DecisionEngineServiceTest.java`

추가할 테스트:

- `pH 9.0 su`가 upper-range threshold로 잡히는지
- `pH`가 corrosion + amber 흐름으로 가는지
- `lead 20 ppb`가 amber + verify-first로 가는지
- `radium 6 pCi/L`가 amber + verify-first로 가는지

## 재개 순서

집에서 다시 시작하면 이 순서로 가면 된다.

1. handoff 문서 확인
2. 템플릿 4개 정리
3. threshold 테스트 추가
4. `.\gradlew.bat test`
5. `8081`에서 Playwright 검증
6. `8081` 종료
7. 기본 상태를 다시 `8080`으로 유지

## 브라우저 검증 규칙

무조건 `8081`에서 확인한다.

최소 체크:

- `/tool/result-first`
- `/tool/symptom-first`
- `/tool/trigger-first`
- threshold-hit result 하나
  - 예: arsenic
  - 예: pH

검증이 끝나면:

- `8081` 서버 프로세스 종료
- 기본 상태는 `8080`

## 다음 세션에서 바로 쓸 프롬프트

집에서 Codex에 아래 프롬프트 그대로 넣으면 된다.

`Continue from 16_RESUME_GUIDE_KO_2026-03-07.md. Finish engine intake/result template cleanup, add pH/lead/radium threshold coverage tests, verify on 8081 with Playwright, then stop 8081 and leave default 8080.`

## 같이 가져가야 하는 것

가장 좋은 방법:

- 레포 전체 폴더를 그대로 가져간다.
- 가능하면 commit/push까지 한다.

최소한 필요한 것:

- 이 문서
- `15_HOME_HANDOFF_2026-03-07.md`
- 현재 worktree 전체

현재는 uncommitted changes가 있기 때문에, 이 문서만 따로 가져가면 부족하다.
반드시 코드 변경 내용도 같이 가져가야 한다.

## 짧은 요약

지금 상태를 한 줄로 정리하면:

`공개 표면과 엔진 구조 업그레이드는 이미 많이 끝났고, 지금은 engine-side UI cleanup + threshold coverage tests + 최종 8081 검증만 남아 있다.`

