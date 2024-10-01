package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
//    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){
//        if(bindingResult.hasErrors())
//        {
//            return "login/loginForm";
//        }
//
//        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
//
//        if(loginMember==null){
//            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 틀립니다.");
//            return "login/loginForm";
//        }
//        //로그인 성공처리
//        /*
//        //만료 날짜를 생략한 쿠키를 생성해서 서블릿 response에 그 쿠키를 담는다.
//        Cookie sessionCookie=new Cookie("memberId",String.valueOf(loginMember.getId()));
//        response.addCookie(sessionCookie);
//        */
//
//        //세션 관리자를 통해 토큰 - value 쌍을 세션 저장소에 저장한다.
//        sessionManager.createSession(loginMember,response);
//
//        return "redirect:/";
//    }

//    @PostMapping("/login")
//    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request){
//        if(bindingResult.hasErrors())
//        {
//            return "login/loginForm";
//        }
//
//        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
//
//        if(loginMember==null){
//            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 틀립니다.");
//            return "login/loginForm";
//        }
//        //로그인 성공처리
//        //세션이 있으면 세션 반환, 없으면 새로 생성
//        HttpSession session=request.getSession();
//        //세션에 로그인 회원 정보를 보관
//        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
//
//        return "redirect:/";
//    }

    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request
    , @RequestParam(defaultValue = "/")String redirectURL){
        if(bindingResult.hasErrors())
        {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember==null){
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 틀립니다.");
            return "login/loginForm";
        }
        //로그인 성공처리
        //세션이 있으면 세션 반환, 없으면 새로 생성
        HttpSession session=request.getSession();
        //세션에 로그인 회원 정보를 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:"+redirectURL;
    }

//    @PostMapping("/logout")
//    public String logout(HttpServletResponse response){
//        expireCookie(response,"memberId");
//        return "redirect:/";
//
//    }

//    @PostMapping("/logout")
//    public String logoutV2(HttpServletRequest request){
//
//        sessionManager.expire(request);
//        return "redirect:/";
//
//    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }
        return "redirect:/";

    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        //쿠키 값이 뭐든간에 상관이 없다.
        cookie.setMaxAge(0);
        //만료시간을 0으로 만들면 해당하는 이름의 쿠키는 사라지게 된다.
        response.addCookie(cookie);
    }
}
