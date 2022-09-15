# [MUCOSA] 대규모 패션 플랫폼 사이트
## 프로젝트 기획
### 프로젝트 설명
> 💡 무신사나 지그재그와 같이 패션카테고리의 제품들을 사용자가 보다 효율적으로 찾을 수 있는 서비스를 제공 <br/>
> ![MUCOSA](https://user-images.githubusercontent.com/59110017/190330199-c923fd20-9d89-45db-bae2-1d357373a8f8.png)

### 설계 고려사항
<details>
<summary>📌 데이터 기준</summary>
<div markdown="1">
<br/>

 1. 상품 데이터 수 : 100만 개
 <br/>
 &nbsp; &nbsp; 여러 패션 플랫폼(ex. 무신사, 브랜디 등)의 의류 상품 수를 확인 후 최대 값으로 결정<br/>
 <br/>

   * 서울스토어 : 약 17만개
   * 브랜디 : 약 100만개
   * 무신사 : 약 20만개
   
   ![Untitled](https://user-images.githubusercontent.com/59110017/190332582-1d3764a9-918f-4975-b226-39f81c07dd21.png)<br/>
 <br/>
 2. 주문 데이터 수 : 약 1670만 개
 ![MUCOSA (1)](https://user-images.githubusercontent.com/59110017/190332840-e827a733-034d-4c1d-917b-d3408549e94e.png)


 
 
</div>
</details>

<details>
<summary>📌 Latency, Throughput 목표</summary>
<div markdown="1">
<br/>

 1. Latency 목표값 설정  
 
  ```
 📢 KISSmetrics는 고객의 47%가 2초 이내의 시간에 로딩이 되는 웹 페이지를 원하고 있으며, 40%는 로딩에 3초 이상 걸리는 페이지를 바로 떠난다고 설명했습니다.
  ```
  
   * 일반적인 경우 : 0.05~0.1초
   * 복잡한 트랜잭션이 필요한 경우 : 2초이내
  
 2. Throughput 목표값 설정
 
  ```
 📢 News1 자료(2021년 기준)에서 쇼핑 플랫폼별 MAU(Monthly Active User, 월간 순수 이용자) 추이는 평균 약 400만 명이다.
  ```
  
   * MAU : 400만(단위 : 명)
   * DAU : 13만(단위 : 명)
   * 안전계수 : 3
   * 1일 평균 접속 수에 대한 최대 피크 때 배율 : 2배<br/><br/>
   ![Untitled (1)](https://user-images.githubusercontent.com/59110017/190335545-856adc4a-17e7-4aaf-8322-dcf580414d5a.png)
   * 1명당 평균 접속 수 : 20회<br/>
   &nbsp; ⇒ 130,000(명) * 20(회) / 86,400(초) * 3(안전계수) * 2(1일 평균 접속 수에 대한 최대 피크 때 배율) = 약 180 rps

  	
</div>
</details>

<details>
<summary>📌 동시성 제어 기준</summary>
<br/>

 ```
 📢 MUSINSA는 직매입한 인기 제품들을 최대 60% 할인하는 ‘무신사 라이브' 행사에서 1초당 최대 동시 접속자 수가 6400명을 기록하였다고 하였으며 이는 약 30여 분 만에 품절이 되었다고 하였습니다.
 ```
 
 1. 1초당 최대 동시 접속자 수 : 6400명
 	
	* 인기 온라인 패션 스토어의 특가 할인 케이스를 참고
	
 2. 시간 당 처리량 : 가용성이 보장되는 범위의 최대치
 
 	* 앞선 Latency의 내용을 참고하여 고객은 가능한 빠른 응답을 원하고 있음
 
 
 
<div markdown="1">
</div>
</details> 

## 아키텍처
![Untitled (2)](https://user-images.githubusercontent.com/59110017/190337828-429460fa-fcd0-43d0-a0d9-88dd2d2ed83f.png)

## 핵심 기능

### 🔍 검색(필터 및 정렬)

> * Query DSL을 통한 상품 검색 기능 구현
> * 쿼리 성능 개선을 통해 모든 페이지에서 1초 이내로 응답(Latency 목표값 달성)
> * 부하테스트를 통해 DB 병목 지점을 확인 후 DB 스케일 업으로 해결

### 👠 상품 주문

> * 대규모 트래픽에서 상품 주문 시 동시성 이슈 발생
> * Pessimistic Lock을 통해서 동시성 제어
> * 성능 개선(다중 컬럼 update 쿼리, N+1)

### 🔔 재입고 알림

> * 사용자의 UX 향상을 위해서 사용자가 관심이 있는 상품에 대해서 재입고 알람을 제공
> * Redis pub/sub 방식을 고려해봤지만 프론트 상의 한계로  SSE로 구현

### 🧑🏻사용자 & 판매자 마이페이지

> * 권한에 따라 기능에 대한 접근을 제한 (USER/ADMIN)
> * 사용자 마이페이지의 경우 사용자의 주문내역을 보여주는 기능
> * 판매자 마이페이지의 경우 품절된 상품내역을 관리하고 재입고를 등록하는 기능


## API 리스트
https://www.notion.so/grazinggoat/API-f628e62f892b4f41a464c22bdfc76f6b

## ERD
![ERD](https://user-images.githubusercontent.com/47559613/186055988-7f0b4c7d-ea35-415e-a322-1dc2a8373c12.png)

