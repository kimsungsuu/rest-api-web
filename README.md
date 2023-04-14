#  📌 rest API Web

## 1. 프로젝트 설명
> rest api 방식의 웹사이트 개발 및 공부

* * *

</br>

## 2. 프로젝트 상세
> - restful 하게 crud, 페이징 API 구현
> - builder 패턴을 사용한 게시글 등록 </br>
> - Java validation을 활용한 데이터 검증
> - ControllerAdvice를 이용한 예외처리 자동화
> - Test
>   - Controller 테스트와 Servlce layer test 분리
>   - mockMvc 방식(json)으로 Controller test
>   - Builder pattern을 사용하여 데이터 생성

<details>
<summary>[👀restful api 코드 자세히]</summary>

```java
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final BoardService boardService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        boardService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Integer postId){
        return boardService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return boardService.getList(pageable);
    }

    @PatchMapping("/posts/{boardId}")
    public void edit(@PathVariable Integer boardId, @RequestBody @Valid BoardEdit boardEdit){
        boardService.edit(boardId, boardEdit);
    }

    @DeleteMapping("/posts/{boardId}")
    public void delete(@PathVariable Integer boardId){
        boardService.delete(boardId);
    }

}
```

- @RestController를 사용
- 자원(resource)을 URI로 명시
- HTTP 메서드(Post, Get, Patch, Delete)를 이용하여 상태 표현
    - 생성, 조회 및 페이징, 수정, 삭제

<hr/>    
    
</details>
</br>

<details>
<summary>[👀builder 패턴 코드 자세히]</summary>

```java
    @Builder
    public SpringBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
```

- builder 패턴을 사용하기 위해 @Builder 적용

```java
  public void write(PostCreate postCreate){

        SpringBoard springBoard = SpringBoard.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

         boardRepository.save(springBoard);
    }
```

- 게시글 생성 메서드에서 builder 패턴 사용
- 매개변수 초기화 과정에서 에러 발생 가능성을 낮춰주고, 가독성을 높여줌.
    
<hr/>    
    
</details>
</br>

<details>
<summary>[👀validation 코드 자세히]</summary>

```java
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;
```

```java
public class PostController {
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        boardService.write(request);
    }
}
```

- 클라이언트 측에서 게시글을 생성할 때 데이터 검증을 하기 위해 validation 사용
- @NotBlank를 사용하여 타이틀과 컨텐츠를 반드시 입력하도록 설정
- validation을 안해도 데이터 검증이 가능하지만 일일히 검증 로직을 작성하는건 유지보수와 가독성을 저하시킴.
- 컨트롤러에서 게시글 작성 메서드에 @Valid를 적용

<hr/>    
    
</details>
</br>

<details>
<summary>[👀ControllerAdvice 코드 자세히]</summary>

```java
@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody // ViewResolver 에러 방지
    public ErrorResponse inValidRequestHandler(MethodArgumentNotValidException e){

        ErrorResponse response =  ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for(FieldError fieldError : e.getFieldErrors()){
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

```

- @ControllerAdvice를 적용한 클래스 생성
- ExceptionHandler를 적용하여 해당 에러처리
- ErrorResponse 응답 클래스로 에러 결과 반환

```java
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;

    private final Map<String, String> validation = new HashMap<>(); 


    @Builder
    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage){
        this.validation.put(fieldName, errorMessage);
    }
}
```

- message 필드는 클라이언트 상요자에게 문제가 발생했음을 alert 하기 위해 생성
- validation 객체는 어떤 field가 error 인지 반환하기 위해 생성.

<hr/>    

</details>
</br>

<details>
<summary>[👀Test 코드 자세히]</summary>

```java
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean(){
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청 시 title 값은 필수")
    void test2() throws Exception {

    //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

    //when
      mockMvc.perform(post("/posts")
              .contentType(APPLICATION_JSON)
              .content(json)
              ).andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.code").value("400"))
              .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
              .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
              .andDo(print());
    }
}

```
- 위 코드는 Controller 테스트에서 게시글 작성 및 데이터 검증의 예시
- builder 패턴을 사용하여 데이터를 생성
- json + mockMvc 방식으로 테스트 코드 작성


```java
@SpringBootTest
class PostServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void clean(){
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void save(){
        // given
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        boardService.write(request);

        // then
        assertEquals(1, boardRepository.count());
        SpringBoard springBoard = boardRepository.findAll().get(0);
        assertEquals("제목", springBoard.getTitle());
        assertEquals("내용", springBoard.getContent());
    }
}
```
- 위 코드는 Service 테스트에서 게시글 작성 테스트 예시
- builder 패턴을 적용하여 데이터 생성
- service에 생성해 놓은 게시글 작성 api(write)를 가져와 테스트 진행
    
<hr/>    
    
</details>

* * *

</br>

## 3. 사용한 기술
> - Java 11
> - Spring boot 2.7
> - Junit 5
> - Spring Data Jpa 3


* * *
