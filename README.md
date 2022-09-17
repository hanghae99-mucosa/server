# [MUCOSA] 대규모 패션 플랫폼 사이트

[![mocusa](https://user-images.githubusercontent.com/47559613/190395847-58c23322-89a6-4e1a-a3dc-0295212b9995.png)](https://youtu.be/T2y4gs5TT-Y)

## 프로젝트 기획
### 프로젝트 설명
> 💡 대규모 트래픽에서 빠른 검색 및 안정적인 주문 서비스를 제공하는 패션플랫폼
> <br/><br/>
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

## 트러블슈팅

<details>
<summary>📈 검색 성능 개선</summary>
<div markdown="1">

- **필요성**
    - 상품 데이터가 100만개로 증가하면서 **메인페이지 로딩 시간이 최대 4.8초까지 증가**

  ⇒ KISSmetrics에 따르면 로딩시간 3초 이상 시 고객의 40% 이탈률 발생

  ![Untitled](https://user-images.githubusercontent.com/31721097/190343898-e73ba4c4-e934-47bc-b404-ec0f699f90c3.png)

  ⇒ 목표 : 페이지 로딩 시간 **2초 이내**

- **진행 단계**
    - 1. Index 생성
        - **적용 계기**

          : 기존 쿼리 실행 시 **order부분**에서 많은 시간이 소요되는 것을 알 수 있었음

          ⇒ Index 적용

        - **인덱스 항목**
            - 리뷰수 (리뷰수, Default 정렬값)

              → 실제 쇼핑몰의 경우 변동이 많은 값이므로 인덱스로 설정하지 않는 것이 좋지만 현재 mucosa의 경우 리뷰 수가 변동이 없는 값이므로 인덱스로 설정하는 것으로 결정

            - 가격 (고가순, 저가순)
        - **결과 분석**
            - 개선된 부분
                - 대부분의 첫 페이지 로딩이 개선되었으며 **최대 2880%까지 개선**
            - 추가 개선이 필요한 부분
                - 일부 항목의 경우 **Count Query에 성능 저하** 발생
    - 2. Cross Join 생성
        - **적용 계기**

          : QueryDSL로 작성한 쿼리가 cross join을 발생시키고 있다는 사실을 알게 됨

          ⇒ cross join 대신 **inner join**이 발생하도록 코드 수정

        - **Cross Join 제거 방법**
            - cross join이 발생하는 brand 테이블을 inner join이 발생하도록 쿼리 수정
        - **결과 분석**
            - **개선된 부분**
                - 키워드 검색 부분의 경우 기존에 발생하던 cross join이 inner join으로 변경되면서 **최대 232%까지 개선**
            - **추가 개선이 필요한 부분**
                - 단순 메인페이지 로딩의 경우 join을 강제하면서 **Count Query에 성능 저하**가 발생
    - 3. 불필요한 Join이 발생하지 않도록 분기처리하여 Count Query 개선
        - **적용 계기**

          : Inner Join을 명시적 처리하면서 brand 테이블 필요하지 않은 count query에서도 join이 발생하여 성능이 떨어진 사실을  발견함

          => 불필요한 join이 발생하지 않도록 **분기 처리**

        - **결과 분석**
            - 개선된 부분
                - Inner Join을 강제하면서 발생했던 count query 성능 저하에 대해서는 **최대 1127%까지 개선**
            - 추가 개선이 필요한 부분
                - **마지막 페이지**에 대한 query 속도는 여전히 느림
    - ~~4. No Offset 방식 적용~~
        - **적용 계기**

          : 페이징 처리 시 offset이 성능 저하를 발생한다는 사실을 알게 됨

          ⇒ NoOffset 방식을 적용

        - **Offset이 성능 저하를 발생시키는 이유**

          : offset을 사용하게 되면 offset + limit만큼의 데이터를 읽은 후 offset만큼의 데이터를 버림

          ⇒ 마지막 페이지로 갈수록 읽어야하는 데이터 수가 비약적으로 증가

        - **NoOffset 방식을 적용하지 않은 이유**
            1. NoOffset 방식의 경우 offset 사용 대신 where절에 조회 시작 부분을 판단하도록 함

               ⇒ Mucosa의 경우 where절에 사용될 조회 시작 부분을 판단하도록 하는 **기준 key가 중복이 가능한 값**(리뷰 개수, 가격)임

            2. NoOffset 방식의 경우 페이징버튼이 아닌 ‘more(더보기)’ 버튼을 사용해야 함 → 순차적 페이지 이동만 가능

               ⇒ Mucosa의 경우 전체 상품 수가 100만개이므로 페이지 이동이 자유로운 **페이징 버튼을 사용**하는 것이 좋을 것으로 판단

    - 5. Covering Index 생성
        - **적용 계기**

          : No Offset 방식을 적용할 수 없는 상황에서 성능을 개선하기 위해 Full Scan이 발생하는 product 테이블 개션의 필요성을 알게 됨

          => Covering Index를 통해 'where, order by, offset ~ limit'를 인덱스 검색으로 빠르게 처리하고 **걸러진 데이터를 통해서만 데이터 블록에 접근**

        - **QueryDSL에서의 Covering 인덱스 적용**

          : QueryDSL의 경우 from절의 서브쿼리를 지원하지 않음

          ⇒ `커버링 인덱스를 활용하여 조회 대상의 PK를 조회`하는 부분과 `해당 PK로 필요한 컬럼 항목들을 조회`하는 부분을 나누어 구현

        - **결과 분석**
            - 개선된 부분
                - 마지막 페이지 속도가 **최대 236%까지 개선**
            - 추가 개선이 필요한 부분
                - 첫페이지에 대해 목표했던 **2초 이내의 결과는 달성하지 못함**
                - **키워드 검색**에 대한 성능 개선이 다른 항목들에 비해 많이 되지 않음
    - ~~6. Full-Text Index 생성 및 match()-against() Query 사용~~
        - **적용 계기**

          : 마지막 페이지 부분이 개선되었으나 처음 목표했던 2초 이내는 달성하지 못함. 그래서 엘라스틱서치 적용도 고려했지만 엘라스틱서치의 역인덱싱 방식과 같은 원리로 동작하는 MySQL의 Full-Text 인덱스를 알게됨

          ⇒ 굳이 엘라스틱 서치를 이용할 필요 없이 Full-Text 인덱스를 적용하고 match()-against() 쿼리문을 사용

        - **Full-Text 인덱스 방식을 적용하지 않은 이유**

          : Full-Text 인덱스를 적용하여 실제 테스트를 해 본 결과 **성능 개선이 되지 않음**

          ⇒ MUCOSA의 경우 검색 시 join이 많이 발생하기 때문

    - 7. 상품명 검색/브랜드명 검색으로 Search Type을 지정하여 검색하도록 변경
        - **적용 계기**

          : Full-Text Index 방식을 시도하면서 join에 의해 성능 저하가 많이 된다는 것을 인지

          => 상품명과 브랜드명을 동시에 검색할 수 있는 로직상 더 이상 join을 줄일 수 없어 **Search Type을 지정하여 검색하는 로직을 반영**하여 join을 줄임

        - **결과 분석**
            - 개선된 부분
                - 검색 기능 성능이 **최대 308%까지 개선**

                  => 첫페이지의 경우 상품명 검색, 브랜드명 검색 **모두 1초 이내로 목표 달성**

                  => 브랜드명 검색의 경우 마지막페이지까지 1초 이내로 목표 달성


- **결과**

  ![Untitled](https://user-images.githubusercontent.com/31721097/190360338-705e078d-1c1b-4acd-9fb8-0c82a4dd0e14.png)


👇🏻**더 자세한 내용이 알고싶다면?**👇🏻

[성능 테스트 및 개선](https://www.notion.so/d08327e203924032879a77930913000e)


</div>
</details>

<details>
<summary>🎮 동시성 제어</summary>
<div markdown="1">

**대안 검증**

| 종류 | 처리 시간 | 리소스 |
| --- | --- | --- |
| Pessimistic | 2.416s | MySQL(RDS) |
| Optimistic | 40.410s | MySQL(RDS) |
| Named | 16.458s | MySQL(RDS) |
| Lettuce | 13.349s | Redis(aws) |
| Redisson | 10.665s | Redis(aws) |

- 트랜잭션의 잦은 충돌 예상

  → **Optimistic lock 부적합**

- Hold and Wait 미충족

  → 데드락 미발생(**Pessimistic lock**의 문제 해결)👈  *후보지1*

- 검색 로직에 대한 영향 최소화

  → version 컬럼을 가지는 것은 row 사이즈가 커지고 이는 검색 속도에 영향(in **Optimistic lock**)

  → DB의 별도 공간이 필요하여 DB 성능에 영향. 즉, 검색 속도 영향(in **Named lock(Use-Level Lock)**)

- 스핀락의 한계

  → 스핀락은 redis server에 부하를 주어 지연 처리 발생(in **Lettuce**)

- Pub/Sub방식의 이점

  → 스핀락의 한계를 pub/sub 기반의 Lock으로 해결(in **Redisson**))👈  *후보지2*


**Pessimistic vs Redission**

![스크린샷 2022-09-14 오전 5 39 53](https://user-images.githubusercontent.com/31721097/190361126-c5d722fe-b245-4699-8e90-14c0d995e09b.png)

→ pub/sub을 통한 lock 획득 시도 과정에서 network latency가 overhead로 발생 추정

→ **최종 Pessimistic lock 적용**

👇🏻**더 자세한 내용이 알고싶다면?**👇🏻

[동시성 제어](https://www.notion.so/a3b60ee964514b03989fc3f687074668)

</div>
</details>

<details>
<summary>📶 부하 테스트</summary>
<div markdown="1">

- **테스트 계기**
    - 대용량 데이터를 처리하는 패션 플랫폼과 같이 많은 이용자들이 사용해 부하가 많이 발생했을 때 시스템이 정상 작동하는지 여부와 응답 성능을 예측하기 위함
- **목표값 설정**
    - [Latency 목표값, Throughput 목표값 설정](https://www.notion.so/MUCOSA-5fe8f0d732234c56b643b24310ab7d33)
- **병목 현상 확인**
    - 조회 성능 검증 단계 진행 중 **rps는 목표값 안**에 들어오지만 **Latency가 급격하게 증가**하고 **Success Rate가 떨어지는 현상** 발견

      ⇒ **DB가 병목 구간**일 것으로 추론

      ⇒ 메인페이지 로딩, 검색 페이지 로딩 시 RDS의 **CPU가 94.47%**까지 상승
    ![MUCOSA (2)](https://user-images.githubusercontent.com/31721097/190361709-bebc8354-5555-471f-9222-f24bfde5167c.png)

- **대안**
    1. ~~일부 Query를 Application단에서 처리함으로써 DB 부하를 낮춤~~
        - 개발 일정상 무리가 있다고 판단
    2. ~~DB Scale Out을 통해 DB 성능 개선~~
        - 개발 일정상 무리가 있다고 판단
    3. **DB Scale Up을 통해 DB 성능 개선**
- **결과**

  ![MUCOSA (3)](https://user-images.githubusercontent.com/31721097/190361725-375137e1-492a-4534-ae69-cd47a43d5f2e.png)

- **결과분석**
    - t3.micro ⇒ **r5.8xlarge** 인스턴스로 교체 후 조회 페이지 테스트 진행 시 **목표했던 Latency와 Throughput을 충족**

👇🏻**더 자세한 내용이 알고싶다면?**👇🏻

[부하 테스트 및 개선](https://www.notion.so/29a9a38935f1441f9e9374fda4d541c1)

</div>
</details>

<details>
<summary>🤖 로깅</summary>
<div markdown="1">

- **로깅 기능의 필요성 및 목표**
    - 애플리케이션 최적화를 위해서 **로직이 작동하는 시간**을 기록 및 측정
    - 로직의 검증을 위해서 사용자의 **요청 및 서버의 응답**을 기록
    - 기존에 작성된 로직에 영향을 끼치거나 로직의 변경이 있으면 안된다.
- **문제점**
    - 로그가 필요한 곳에 일일이 로그 로직을 작성해야 한다.
    - 중복된 로그 로직 때문에 유지보수 및 업데이트 비용이 발생한다.
- **문제 해결**
    - 로그 기능을 횡단 관심사(부가 기능)라고 판단 **AOP**를 사용하여 일관성 있는 로직을 구현

👇🏻**더 자세한 내용이 알고싶다면?**👇🏻

[로깅](https://www.notion.so/8596fa32fec940389c487c1863f03bab)

</div>
</details>

<details>
<summary>🔔 재입고 알림</summary>
<div markdown="1">

- **적용 계기**
    - 재고가 떨어진 상품에 대해서 고객들은 빠르게 재입고가 되었음을 알고 싶다.
- **대안**
    - **SSE**
      기존의 http 연결을 유지하여 서버에서 클라이언트로 데이터를 보낼 수 있다.
    - Redis
      Redis PUB/SUB 방식으로 실시간 양방향 통신 가능
- **문제점**
    - 클라이언트 연결 문제
        - 프론트 부분은 SSR이며, Vanilla JS로 연결을 하고 있어 페이지 전환시 재연결 필요
    - 다중 서버로 인한 알림 송수신 불완전성
        - 연결 요청을 한 서버와 알림을 송신하는 서버가 다른 경우 정상적인 알림 송수신 불가
- **대처**
  여러 가지 방법을 모색했으나 알람 기능을 구현했을 때의 효과보다 기능 구현에 드는 비용(Front를 구성하는 작업)이 크다고 판단
  **⇒ SSE의 구현으로 마무리**

👇🏻**더 자세한 내용이 알고싶다면?**👇🏻

[재입고 알림](https://www.notion.so/147cea2dbac14758ae3cac334daf8273)

</div>
</details>

## 프로젝트 관리
<details>
<summary>지속적인 배포(CD)</summary>
<div markdown="1">

   * 지속적인 배포의 필요성
     * 기능이 추가될 때마다 배포해야하는 불편함이 있어 배포 자동화의 필요성 인식
   * 대안
   
     |Jenkins|Github Actions|
     |------|------|
     |무료|일정 사용량 이상 시 유료|
     |작업 또는 작업이 동기화되어 제품을 시장에 배포하는데 더 많은 시간이 소요|클라우드가 있으므로, 별도 설치 필요 없음|
     |계정 및 트리거를 기반으로하며 Github 이벤트를 준수하지 않는 빌드를 중심으로 함|모든 Github 이벤트에 대한 작업을 제공하고 다양한 언어와 프레임워크를 지원|
     |전 세계 많은 사람들이 이용해 문서가 다양|젠킨스에 비해 문서가 없음|
     |캐싱 메커니즘을 지원하기 위해 플러그인 사용 가능|캐싱이 필요한 경우 자체 캐싱 메커니즘을 작성해야함|
     
   * 선택
     * Jenkins가 Github Actions에 비해 별도의 서버 설치가 필요하는 등 인프라 세팅이 까다롭긴 하나 무료이고 다양한 기능들을 제공하기 때문에 선택했음
	
</div>
</details>
<details>
<summary>코딩 컨벤션</summary>
<div markdown="1">
<br/>

   * 코딩 컨벤션의 필요성
     * 정해진 규칙없이 협업을 하다보니 팀원 들의 코드를 이해하기 어려웠고 Git에서 Merge할 때 어려움이 있어서 코딩 컨벤션의 필요성 인식
   * 코딩 컨벤션 장점
     1. 정해진 규칙이 있기 때문에 명칭이나 구조를 빠르고 정확하게 정할 수 있다.
     2. 통일된 규약이 있기 때문에 모든 사람들이 코드를 이해하기 쉽고 편리하다.
     3. 유지보수 비용을 줄일 수 있다.
     
   👇🏻더 자세한 내용이 알고싶다면?👇🏻<br/>
    &nbsp; 🔧&nbsp; [코딩 컨벤션](https://www.notion.so/grazinggoat/Spring-Boot-ae0d493d298d486ab6921f5859e8caba)

</div>
</details>
<details>
<summary>Git</summary>
<div markdown="1">
<br/>

   * Git Commit 메시지 컨벤션의 필요성
     * commit된 코드가 어떤 내용을 작성 했는 지 파악하려면 commit을 확인해야 한다.
     * 프로젝트 진행 중에는 수 많은 코드가 commit되기 때문에 일일이 내용을 확인하기 힘들기 때문에 
메시지 컨벤션을 통해서 제목이나 description을 통해서 commit의 정보를 전달한다.
   * Git Commit 메시지 컨벤션 전략
   
   ```
   Feat : 새로운 기능에 대한 커밋
   Fix : 기능에 대한 버그 수정에 대한 커밋
   Build : 빌드 관련 파일 수정에 대한 커밋
   Chore : 그 외 자잘한 수정에 대한 커밋(기타 변경)
   Ci : CI 관련 설정 수정에 대한 커밋
   Docs : 문서 수정에 대한 커밋
   Style : 코드 스타일 혹은 포맷 등에 관한 커밋
   Refactor : 코드 리팩토링에 대한 커밋
   Test : 테스트 코드 수정에 대한 커밋
   ```
   
 👇🏻더 자세한 내용이 알고싶다면?👇🏻<br/>
    &nbsp; 🚥 &nbsp; [Git](https://www.notion.so/grazinggoat/Git-a47bf6fa98f143a899b2efbb78c50c85)
</div>
</details>


## 설계
&nbsp; 📖 &nbsp; [API 설계](https://www.notion.so/grazinggoat/API-f628e62f892b4f41a464c22bdfc76f6b)  
&nbsp; 🗃️ &nbsp; [DB 설계](https://www.notion.so/grazinggoat/DB-8de3ac23def0400caa0dc6e399d53984)

## 팀원

|이름|포지션|분담|@ Email|Github|
|------|------|------|------|------|
|김동일|BackEnd|검색(쿼리 최적화)<br/>데이터 생성<br/>CD<br/>부하 테스트|ehddlf618@gmail.com|https://github.com/dongil618|
|박소윤|BackEnd|검색(쿼리 최적화) <br/>사용자/판매자 마이페이지<br/>주문 취소<br/>부하 테스트|parksoyun98@naver.com|https://github.com/dongil618|
|백승찬|BackEnd|회원가입<br/>로그인<br/>재입고 알림<br/>상품 주문(동시성 제어)<br/>로깅|bsc980504@outlook.com|https://github.com/Backseungchan|
|주유찬|BackEnd|재입고 알림<br/>상품 주문(로직)<br/>상품 주문(동시성 제어)<br/>로깅|jjucc99@naver.com|https://github.com/jjucc99|
