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
<details>
<summary>Front-End</summary>
<div markdown="1">   
문제 : 코인 종목 교체 선택 시 기존에 구독하고 있던 종목에 대한 실시간 데이터 구독이 해제되지 않아 차트에 2가지 종목에 대한 현재가 정보 입력</br>
원인 : 구독 상태에서 데이터가 수신되는 시점에 구독 해제를 위한 subsrcibe ID를 교체하도록 되어 있어 데이터가 수신되지 않을 경우 useRef의 ID 교체 불가</br>
해결 : 구독 시작 시점에 ID 정보를 useRef에 저장 시킨 후 구독하여 데이터 수신이 없어도 구독 해제가 정상적으로 이루어질 수 있도록 변경</br></br>
  
문제 : 모의투자 페이지 이용 시 채팅 및 현재가 정보가 페이지 로드 후 새로고침해야 정상 동작</br>
원인 : 웹소켓 커넥트가 완료되기 전 구독을 시도하는 경우 구독이 이루어지지 않은 상태로 유지</br>
해결 : Redux에 웹소켓 연결 여부를 체크하는 상태 값(chkConneted)를 추가하여 상태 변경이 되는 경우 데이터를 수신하는 컴포넌트에서 구독을 진행하도록 개선</br>

</div>
</details>

<details>
<summary>Back-End</summary>
<div markdown="1">   
  
</div>
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
