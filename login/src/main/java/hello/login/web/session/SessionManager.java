package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String,Object> sessionStore=new ConcurrentHashMap<>();


    /**
     *
     * 세션아이디를 만들고,
     * 세션 저장소에 세션 아이디 - value 쌍을 저장하고,
     * 세션 아이디로 응답 쿠키를 만들어 클라에 전달
     * @param value
     * @param response
     */
    public void createSession(Object value, HttpServletResponse response){
        //세션 아이디를 생성하고 store에 저장
        String sessionId= UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);

    }

    //세션 저장소에서 세션id를 key로 해서 객체를 꺼내옴.
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie==null){
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies=request.getCookies();
        if(cookies==null){
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie->cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    //세션 저장소에 있는 세션을 만료시킴
    public void expire(HttpServletRequest request){
        Cookie sessionCookie=findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie!=null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }
}
