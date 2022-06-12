# 코린이(corinne) - 코인 모의투자 연습사이트

![로고 예제1](https://user-images.githubusercontent.com/42165194/170652570-58f367d8-dd2a-4c6e-9ff1-880decb4eb3e.png)

### 도메인: [corinne](https://www.corinne.kr)

<br/>

## :calendar: 프로젝트 기간

2022년 4월 22일 ~ 2022년 6월 3일 (4주)

## :clipboard: 프로젝트 소개

코린이들을 위한 모의투자 corinne

corinne는 코인 초보자분들이 쉽고 재미있게 접할 수 있도록 다양한 기능들을 제공하는 코인 모의투자 서비스입니다.

### 👉 자세히 보기: [노션링크](https://believed-tuba-3d0.notion.site/corinne-2a77c90129b646baacbc3365845d135a)

<br/>

## 서비스 아키텍처

![서비스 아키텍처](https://user-images.githubusercontent.com/93954839/170642303-5fc5675f-37a7-450b-9611-3710fcb410eb.PNG)

## 기술스택

#### :boom: Frontend

next.js, vercel, redux, tailwindCSS, PostCSS, sockjs, stomp, ApexChart, Chart.js

#### :boom: Back-end

SpringBoot, SpringSecurity, Socket.io, MySQL, Redis, AWS S3, Github Actions,  AWS CodeDeploy,  NGINX,  AWS EC2

## API 설계

#### 👉 자세히 보기: [노션링크](https://www.notion.so/a0ea3128fff149368cce366a0ee1462d?v=d003aab079944f109f825390bff57adc)

## ERD
<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">
  
![ERD](https://user-images.githubusercontent.com/95765861/173230013-383dbe45-3e14-4af1-81b4-4b000da151f4.png)
  
</div>
</details>

## Trouble Shooting
<details markdown="1">
<summary>N+1 문제</summary>
  
### ✅ 문제상황

> 유저 랭킹리스트 데이터를 뽑기 위해 findAll 풀스캔 조회할 경우 보유 코인 정보(하위엔티티)를 찾는 쿼리가 N개 추가로 발생.  

### ✅ 해결방안

> 1. fetch join을 이용해 user정보를 찾을 때 coin 정보를 같이 찾는다.
> 2. @EntityGraph 어노테이션을 이용해 user 정보를 찾을 때 coin 정보를 같이 찾는다.

### ✅ 결과
> 해결 전 
![image](https://user-images.githubusercontent.com/95765861/173232176-d2ac8cf1-b49b-4297-a2af-4ae8f95f9ca9.png)

> 해결 후
![image1](https://user-images.githubusercontent.com/95765861/173232182-0f68ba89-d3b7-482c-962c-d085a32cbd4c.png)

> 6,000 row 기준 49,198ms 에서 505ms 로 성능 개선

</details>
<details markdown="1">
<summary>코인 매수, 매도 동시 api 요청 문제</summary>
  
### ✅ 문제상황

> 코인 구매, 판매 api  테스트 중,  동시 요청이 들어오면 해당 유저 보유 계좌 금액 데이터에 변질 문제 발생.  

### ✅ 해결방안

> 1. JPA를 사용하면 READ COMMITTED 이상의 격리 수준이 필요할 때 비관적 락 혹은 낙관적 락을 선택해야 한다.

### ✅ 결과
> 총 판매 갯수 한정 되어 있는 상품과는 달리 한도가 없는 코인 구매는 충돌 발생 확률이 낮다고 판단되어  버전 관리를 통한 낙관적 락 적용   
![image](https://user-images.githubusercontent.com/95765861/173232723-aed2440f-1f07-46c9-ac4a-0127521ca17b.png)


</details>
<details markdown="1">
<summary>double 자료형 연산 오차</summary>
  
### ✅ 문제상황

>12.23와 34.45을 더했으니 결과로 46.68을 예상했겠지만, 실제로는 46.68000000000001가 출력되는 문제 발생    
![image](https://user-images.githubusercontent.com/95765861/173233250-f0018598-ab8d-4b37-94f7-cb3f0b47deda.png) 

### ✅ 해결방안

> 부동 소수점 표현 방식의 오차를 해결하기 위해 자바에서는 BigDecimal 클래스를 제공한다.


### ✅ 결과
> 46.68오차 범위 해결  
![image](https://user-images.githubusercontent.com/95765861/173233432-d750ae77-f576-4b61-90ba-c090fbf8442d.png)



</details>

<details markdown="1">
<summary>Jasypt를 이용한 암호화 적용시 발생한 문제</summary>
  
### ✅ 문제상황

> git actions를 활용한 CI/CD 구현 중에 주요 정보가 들어있는 application.properties를 github push 해야되는 상황

### ✅ 해결방안

> 1. Jasypt 클래스를 이용하여 암호화

### ✅ 결과
> 암호화 결과  
![image](https://user-images.githubusercontent.com/95765861/173232641-8a10a8aa-f039-4310-b78f-30f9e0e8c187.png)

</details>


## UI

<details>
<summary>여기를 눌러주세요</summary>
<div markdown="1">   

#### 메인페이지

![메인페이지](https://user-images.githubusercontent.com/93954839/170641474-02c4b7c7-5a94-450f-b026-a34d94643801.PNG)

#### 모의투자페이지

![모의투자화면](https://user-images.githubusercontent.com/93954839/170641555-55b3c709-ad0a-4475-a030-fa5c4871845e.PNG)

#### 랭킹페이지

![랭킹페이지](https://user-images.githubusercontent.com/93954839/170641525-ac36933e-cd80-4cf6-a462-f091431c2816.PNG)

#### 마이페이지

![마이페이지](https://user-images.githubusercontent.com/93954839/170641538-59df30c8-a305-4006-8b44-c2abade7a418.PNG)
  
</div>
</details>

## 팀원소개

| Name                 | GitHub / Contact                       | Position    |
| -------------------- | -------------------------------------- | ----------- |
| Frontend Github Link | https://github.com/suns2131/corinne_fe |
| 윤선식VL             | https://github.com/suns2131            | FE / React  |
| 원동환               | https://github.com/endol007            | FE / React  |
| Backend Github Link  | https://github.com/GyuwonY/corinne_BE  | API Repository |
|                      | https://github.com/GyuwonY/coin_data   | Socket client Repository |
| 유규원L              | https://github.com/GyuwonY             | BE / Spring |
| 정제무               | https://github.com/Jemoo1060           | BE / Spring |     |
