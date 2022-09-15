# [MUCOSA] 대규모 패션 플랫폼 사이트
## 프로젝트 기획
### 프로젝트 설명
> 💡 무신사나 지그재그와 같이 패션카테고리의 제품들을 사용자가 보다 효율적으로 찾을 수 있는 서비스를 제공	
> ![MUCOSA](https://user-images.githubusercontent.com/59110017/190330199-c923fd20-9d89-45db-bae2-1d357373a8f8.png)

### 설계 고려사항
<details>
<summary>📌 데이터 기준</summary>
<div markdown="1">
 1. 상품 데이터 수 : 100만 개
 여러 패션 플랫폼(ex. 무신사, 브랜디 등)의 의류 상품 수를 확인 후 최대 값으로 결정<br/>
 
  &nbsp;
   * 서울스토어 : 약 17만개
   * 브랜디 : 약 100만개
   * 무신사 : 약 20만개
   
   ![Untitled](https://user-images.githubusercontent.com/59110017/190332582-1d3764a9-918f-4975-b226-39f81c07dd21.png)<br/>
 2. 주문 데이터 수 : 약 1670만 개
 ![MUCOSA (1)](https://user-images.githubusercontent.com/59110017/190332840-e827a733-034d-4c1d-917b-d3408549e94e.png)


 
 
</div>
</details>
<details>
<summary>📌 Latency, Throughput 목표</summary>
<div markdown="1">
</div>
</details>
<details>
<summary>📌 동시성 제어 기준</summary>
<div markdown="1">
</div>
</details> 


## 개발환경
![항해99 이노베이션 실전 프로젝트 1차 최종발표](https://user-images.githubusercontent.com/47559613/186055514-a4ec060e-1f00-4d25-bee2-5d3f04bc71a4.png)

## 주요기능
![이미지 003](https://user-images.githubusercontent.com/47559613/186055518-2e025cec-481f-419b-b875-d7d6bb4a4756.png)
![이미지 004](https://user-images.githubusercontent.com/47559613/186055509-ee7331cd-845b-4969-879c-c9ff07ef3cdd.png)

## API 리스트
https://www.notion.so/grazinggoat/API-f628e62f892b4f41a464c22bdfc76f6b

## ERD
![ERD](https://user-images.githubusercontent.com/47559613/186055988-7f0b4c7d-ea35-415e-a322-1dc2a8373c12.png)

## Architecture
![이미지 002](https://user-images.githubusercontent.com/47559613/186055515-853f0ec1-ad69-432d-8d33-d910451216d8.png)

# Github Flow

```powershell
main : 제품으로 출시될 수 있는 브랜치
develop : 다음 출시 버전을 개발하는 브랜치
feature/#notification_list : 기능을 개발하는 브랜치**
release : 이번 출시 버전을 준비하는 브랜치
hotfix/#notification_list : 출시 버전에서 발생한 버그를 수정 하는 브랜치
----------------------------------------------------------------------------------------
1. main 브랜치는 항상 안정된 빌드이자 사용자에게 서비스중인 빌드입니다.
2. 모든 feature 브랜치는 develop 브랜치에서 클론한다.
3. feature 브랜치는 기능 단위로 구분한다.
4. feature 작업이 끝나면 develop branch로 pull request 한다.
5. develop branch에서 모든 취합이 끝나면 main으로 pull request 한다.
6. merge 후 브랜치는 삭제한다.
```


# Git Commit 메시지 컨벤션

### **Commit 컨벤션**

```powershell
$ <type>(<scope>): <subject>    -- 헤더
  <BLANK LINE>                  -- 빈 줄
  <body>                        -- 본문
  <BLANK LINE>                  -- 빈 줄
  <footer>                      -- 바닥 글
```

```
Feat : 새로운 기능에 대한 커밋
Fix : 기능에 대한 버그 수정에 대한 커밋
Build : 빌드 관련 파일 수정에 대한 커밋
Chore : 그 외 자잘한 수정에 대한 커밋(기타 변경)**
Ci : CI 관련 설정 수정에 대한 커밋
Docs : 문서 수정에 대한 커밋
Style : 코드 스타일 혹은 포맷 등에 관한 커밋
Refactor : 코드 리팩토링에 대한 커밋
Test : 테스트 코드 수정에 대한 커밋
```

### commit 메시지 규칙

1. 제목과 본문을 빈 행으로 구분합니다.
2. 제목을 50글자 이내로 제한합니다.
3. 제목의 첫 글자는 대문자로 작성합니다.
4. 제목의 끝에는 마침표를 넣지 않습니다.
5. 제목은 명령문으로! 과거형을 사용하지 않습니다.
6. 본문의 각 행은 72글자 내로 제한합니다.
7. 어떻게 보다는 무엇과 왜를 설명합니다.


---

## 최종 컨벤션

### Controller

```java
//페이지관련 controller
@Controller 
class ????Controller(){
    public String methodName(RequestDto requestDto){
			...
			return new ResponseEntity(responseDto, HttpStatus.OK);
		}
}

---------------------------------------------------------------------------------------

//응답관련 controller
@RestController 
@RequiredArgsConstructor
class ObjectApiController(){ // Object는 다루는 Model 이름으로	

		// 의존성 주입은 무조건 final
		private final ObjectService objectService;

		/* DTO -> Model은 무조건 서비스에서 */
    public ResponseEntity<ResponseDto> methodName(RequestDto requestDto){
			...
			return new ResponseEntity(responseDto, HttpStatus.OK);
		}
}
```

### Service

```java
@Service
class ObjectService(){

		// 리턴은 DTO로 변경해서
    public ResponseDto methodName(RequestDto requestDto){
			Model model = requestDto.toModel();
			// toModel()의 내부 구현은 builder
			// Model model = this.DtoToModel(requestDto)
...
			return new ResponseDto(model);
		}
}
```

### DTO

```java
@Getter
@NoArgsConstructor
@AllArgsConstructor
class DTO (){
}

/*
dto naming -> ActionRequestDto & ActionResponseDto
*
```

### Model

```java
//Lombok -> Setter 제외, 필용한 경우에는 직접 구현
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
class Model(){
}
```

### Exception

```java
// 예외발생 시, if else, try catch
// -> throw new RuntimeException()
// 에러는 상속 받아서 케이스에 맞게 만들겁니다.
// 만약 keyword가 중복이다,
// -> throw new BookmarkDupl();
```

---

# **객체지향 생활체조 원칙**

- [**규칙 1. 한 메서드에 오직 한 단계의 들여쓰기만 한다**](https://limdingdong.tistory.com/7)
- ****[규칙 2. else 예약어를 쓰지 않는다](https://limdingdong.tistory.com/8)****
- ****[규칙 3. 모든 원시값과 문자열을 포장한다](https://limdingdong.tistory.com/9)****
- ****[규칙 4. 한 줄에 점을 하나만 찍는다](https://limdingdong.tistory.com/10)****
- [~~규칙 5. 줄여쓰지 않는다~~](https://limdingdong.tistory.com/11)
- [~~규칙 6. 모든 엔티티를 작게 유지한다~~](https://limdingdong.tistory.com/12)
- [~~규칙 7. 2개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다~~](https://limdingdong.tistory.com/13)
- [~~규칙 8. 일급 컬렉션을 쓴다~~](https://limdingdong.tistory.com/14)
- ****[규칙 9. getter/setter/property를 쓰지 않는다](https://limdingdong.tistory.com/15)****
