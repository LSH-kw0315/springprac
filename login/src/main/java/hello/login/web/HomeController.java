package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentResolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
//    public String home() {
//
//        return "home";
//    }
//    @GetMapping("/")
//    public String homeLogin(@CookieValue(name="memberId",required=false)Long memberId, Model model){
//
//        if(memberId==null) {
//            return "home";
//        }
//
//        Member loginMember=memberRepository.findById(memberId);
//        if(loginMember==null){
//            return "home";
//        }
//
//        model.addAttribute("member",loginMember);
//        return "loginHome";
//    }

//    @GetMapping("/")
//    public String homeLoginV2(HttpServletRequest request, Model model){
//
//        //세션 관리자의 세션 저장소를 조회
//        Member member=(Member)sessionManager.getSession(request);
//
//        //기존에는 멤버DB를 조회했지만, 그럴 필요가 없다.
//        if(member==null){
//            return "home";
//        }
//
//        model.addAttribute("member",member);
//        return "loginHome";
//    }
//    @GetMapping("/")
//    public String homeLoginV3(HttpServletRequest request, Model model){
//
//        HttpSession session=request.getSession(false);
//        if(session==null){
//            return "home";
//        }
//
//        //세션 관리자의 세션 저장소를 조회
//        Member member=(Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
//
//        //기존에는 멤버DB를 조회했지만, 그럴 필요가 없다.
//        if(member==null){
//            return "home";
//        }
//
//        model.addAttribute("member",member);
//        return "loginHome";
//    }
//    @GetMapping("/")
//    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false)Member member, Model model){
//
//        //세션에 회원 데이터가 없으면 home
//        if(member==null){
//            return "home";
//        }
//
//        model.addAttribute("member",member);
//        return "loginHome";
//    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member member, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }
}