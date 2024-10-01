package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager=new SessionManager();
    @Test
    public void sessionTest(){
        MockHttpServletResponse response=new MockHttpServletResponse();

        //세션 생성. 세션 저장소에는 생성된 세션 아이디와 member가 쌍으로 저장되게 된다.
        Member member=new Member();
        sessionManager.createSession(member,response);

        //요청에 서버가 준 쿠키를 저장 (지금 뭐 정상적인 통신을 하는 상태가 아니므로)
        //그 이후에 서버에 요청한다고 가정
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //요청 메시지의 쿠키로 세션 저장소를 조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired=sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();
    }



}