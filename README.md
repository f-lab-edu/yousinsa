# YOUSINSA
MUSINSA 같은 온라인 편집샵 대용량 서버

## Mock-Up

[Main - No User (1 of 20)](https://ovenapp.io/project/PGw27rPWTmydr8mpmbRVAZYTZurQXuV6#7YCsA)

## Design

### Table
[Table 설계에 대한 Issue](https://github.com/f-lab-edu/yousinsa/issues/5)

[Table Diagram](https://dbdiagram.io/d/626c11c695e7f23c619ca37d)

<img src="https://user-images.githubusercontent.com/25685282/166242241-7685315d-fc17-4bc4-abce-a65d3a71c318.png" alt="">

---
## 👕 프로젝트 중점사항

프로젝트를 진행하면서 중점적으로 도입해 볼 목록을 정리했습니다.

### Common

- Version 관리 전략
- 문서화

### Spring

- Spring 기능을 충분히 활용
- Spring 내부 동작과 구조를 숙지하면서 사용
- 글로벌 서비스 기준

### Performance

- 서버 확장성
- 대규모 트래픽을 처리에 대한 고려
- 비동기 처리를 경험해 볼 수 있도록
- 테이블 설계에 대한 고려 사항 체험

### Code Quality

- Code Convention을 준수
- OOP와 관련된 원칙들을 준수
- 테스트가 쉽도록 설계 준수
- Layer에 대한 구분 준수

### 문제 상황 Simulation

- 한정 판매로 정해진 수량의 물품만 판매 - 수량의 제한
- 주문 데이터가 많은 경우 정산에 대한 처리 시간 문제
- 이벤트 시 한번의 트래픽이 몰리는 경우
- 어쩔 수 없이 서버가 다운되는 경우에 대한 Fail-Over 테스트
- 비동기적인 처리를 통해 처리 속도가 향상되어야 하는 문제

---

## 👖 UseCase

필요한 정책이 추가적으로 생길 수 있습니다.

### Common

- user는 `회원 가입`을 통해 Role을 획득할 수 있다.
- user는 `회원 탈퇴`를 할 수 있다.
- user는 `로그인`을 통해 서비스를 사용할 수 있다.
- user는 `로그아웃` 을 통해 서비스 사용을 종료할 수 있다.

### Buyer

- buyer는 `물품 목록 조회`를 할 수 있다.
- buyer는 `물품 상세 조회`를 할 수 있다.
- buyer는 `물품을 장바구니에 추가` 할 수 있다.
- buyer는 `물품을 장바구니에서 제거` 할 수 있다.
- buyer는 `물품을 즐겨찾기` 할 수 있다.
- buyer는 `물품 구매`를 할 수 있다.
- buyer는 `물품 구매 취소`를 할 수 있다.
- buyer는 `주문 내역 조회`를 할 수 있다.
- buyer는 `이벤트 참여`를 할 수 있다.

### Store Owner

- store owner는 `입점 신청` 을 진행할 수 있다.
- store owner는 입점이 완료되면 `물품 등록` 을 진행할 수 있다.
- store owner는 신청된 `주문을 수락` 할 수 있다.
- store owner는 신청된 `주문을 취소` 할 수 있다.
- store owner는 `해당 샵에서 진행하는 이벤트를 개최` 할 수 있다.
- store owner는 `매출 통계` 를 통해 월별 매출, 품목별 매출을 확인할 수 있다.

### Admin

- admin은 `입점 신청을 수락`할 수 있다.
- admin은 `모든 User의 정보를 조회` 할 수 있다.

---

## 🩳 Version Definition

- v1 : 기획 구현과 테스트를 중점으로 개발
    - Unit Test
    - Integration Test


- v2 : 리팩토링(구조 개선)
    - 확장에 유연하도록 개선
    - 관련된 Spring Module 적용


- v3 : 성능 개선(극한 상황에 대한 테스트)
    - 부하 테스트
    - 쿼리 최적화
    - MSA로 가기 위한 준비


- v4 : Monolithic to MSA(MicroService Architecture)
    - Monolithic보다 더 유연성 있는 구조와 패턴 적용(Scale-out 고려)
    - MSA와 관련된 Test
    - CQRS

---

## 🦺 Project History

<details>
<summary>[2022.06.10] Version에 대한 정의 추가</summary>
<div markdown="1">

- [x] Version(Phase)에 대한 정의 추가
    - Version 1, 2, 3, 4


- [x] v1 Schedule에 대한 부분 등록
    - https://github.com/f-lab-edu/yousinsa/milestones

</div>
</details>

<details>
<summary>[2022.04.13] 시작 ReadMe 작성</summary>
<div markdown="1">

- [x]  Mock-Up 만들기(04/10일 내로 완료 후 취합, 4/11 멘토님에게 검토)
- [x]  Naming 결정 - 마신사, 유신사
- [x]  Category - 상의, 하의, 아우터 (이 안에서도 추리기)
- [x]  우리만의 프로젝트 중점 사항 정하기

**[예시]**

       - Spring MVC 기능을 충분하고 잘 활용하기
       - Coding Convention 정하기
       - OOP와 관련된 원칙들을 준수
       - 테스트가 쉬운 코드를 작성
       - 백엔드 실무에서 발생할 수 있는 문제를 해결할 수 있도록 설계하기

- [x]  Role 정리
- [x]  문제 상황 Simulation

**[예시]**

       - 한정 판매로 정해진 수량의 물품만 판매 - 수량의 제한
       - 결제 데이터가 많은 경우 정산에 대한 처리 시간 문제
       - 이벤트 시 한번의 트래픽이 몰리는 경우
       - 결제 도중 시스템이 다운될 경우에 대한 결제에 대한 롤백 처리
       - 어쩔 수 없이 서버가 다운되는 경우에 대한 Fail-Over 테스트

</div>
</details>
