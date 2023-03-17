#  📌 rest API Web

## 1. 프로젝트 설명
> rest api 방식의 웹사이트 개발 및 공부

* * *

</br>

## 2. 프로젝트 상세
> - CRUD, 페이징
>   - restful 방식으로 crud, 페이징 API 구현
> - 데이터 검증(@Valid)
> - Test
>   - Controller 테스트와 Servlce layer test 분리
>   - mockMvc 방식(json)으로 Controller test
>   - Builder pattern을 사용하여 데이터 생성
>   - 수정 필드 정보를 갖고 있는 클래스와 실질적인 수정을 수행할 builder 클래스를 분리

* * *

</br>

## 3. 사용한 기술
> - Java 11
> - Spring boot 2.7
> - Junit 5
> - Spring Data Jpa 3


* * *

</br>

## 4. 트러블 슈팅/ 문제 개선
> - BindingResult 클래스를 사용한 에러 처리 시 반복 작업 가능성 우려
>   - @ControllerAdvice 어노테이션을 사용하여 해당 필드 에러 처리 자동화
>   - [코드확인](https://github.com/ksungsu/rest-api-web/blob/ea8399d1dc17394b6ec28c1483d7e434a5db43a2/src/main/java/com/sungsu/controller/ExceptionController.java#L16)

* * *

</br>

## 5. 기타 백엔드 개발
> - entity와 dto(사용자 요청에 응답하는 데이터 필드)의 분리
> - @ExceptionHandler와 @ControllerAdvice를 사용한 에러 처리 구현
>   - BindingResult 객체를 이용한 반복적인 에러 정보 처리를 자동화합니다.
>   - response 클래스를 만들어 code, message 필드와 validation 컬렉션 객체를 생성하고 클라이언트에 데이터가 넘어가도록 설정하고, Test 코드를 수정합니다.
> - 코드 리팩토링
>   - 클라이언트와 백엔드 데이터 넘김 방식(id, 또는 전체 데이터 넘겨주기 또는 아무 데이터도 넘기지 않기)

