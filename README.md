#  📌 rest API Web

## 1. 프로젝트 설명
> rest api 방식의 웹사이트 개발 및 공부

* * *

</br>

## 2. 프로젝트 기능
> CRUD
> 데이터 검증(@Valid)
> rest API 구축
> MockMvc 테스트(단위 테스트)

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
> - HTTP API에 대한 이해
>   - Json 데이터 교환, @RequestBody를 이용한 HTTP Body 데이터 사용
> - entity와 dto(사용자 요청에 응답하는 데이터 필드)의 분리
>   - entity : DB와 데이터를 교환하는 필드, 주로 service 계층에서 사용
>   - dto : Controller에서 http 요청 데이터를 처리할 때 사용
> - @Valid와 @ControllerAdvice를 사용한 에러 처리 구현
>   - BindingResult 객체를 이용한 반복적인 에어 정보 처리를 자동화합니다.

