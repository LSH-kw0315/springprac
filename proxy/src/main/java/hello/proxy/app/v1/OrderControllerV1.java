package hello.proxy.app.v1;


import org.springframework.web.bind.annotation.*;

@RestController //스프링은 @Controller 혹은 @RequestMapping이 있어야 컨트롤러로 인식
public interface OrderControllerV1 {
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
