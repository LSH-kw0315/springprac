package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList={"/","/members/add","/login","/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String requestURI=request.getRequestURI();

        HttpServletResponse response=(HttpServletResponse) servletResponse;

        try{
            log.info("인증 체크 필터 시작{}",requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행{}",requestURI);
                HttpSession session=request.getSession(false);

                if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
                    //그 사용자에 대한 세션이 없거나, 세션은 있으나 로그인 기록은 없는 경우
                    log.info("미인증 사용자의 요청 {}",requestURI);

                    response.sendRedirect("/login?redirectURL="+requestURI);
                    //로그인 페이지로 보내는데, 뭔 경로를 남겨놓았다.
                    //로그인이 성공했을 때 다시 원래 접근하려 했던 페이지로 갈 수 있게 리다이렉트 경로를 쿼리 스트링으로 남겨놓는다.
                    return;
                }
            }

            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){
            throw e; //예외를 잡을 수는 있지만, 톰캣까지 보내주어야한다.
        }finally{
            log.info("인증 체크 필터 종료 {}",requestURI);
        }
    }
    //화이트리스트 경로 필터 체크 제외
    public boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI);
    }
}
