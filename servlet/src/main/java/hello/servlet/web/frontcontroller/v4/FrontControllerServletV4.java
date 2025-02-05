package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name="frontControllerServletV4",urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap=new HashMap<>();

    public FrontControllerServletV4(){
        controllerMap.put("/front-controller/v4/members/new-form",new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save",new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members",new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontController");

        String requestURI=req.getRequestURI();

        ControllerV4 controller=controllerMap.get(requestURI);

        if(controller==null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Map<String,String> paramMap=createParamMap(req);
        Map<String,Object> model=new HashMap<>(); //modelView를 반환받는 대신, 인자로 넘어간 model의 변화가 생기도록 한다.

        String viewName=controller.process(paramMap,model); //process를 통해 논리 주소가 반환된다.

        viewResolver(viewName) //viewResolver로 실제 물리 주소가 매핑된 MyView 반환
                .render(model,req,resp); //MyView의 오버로딩된 render 실행

    }

    private Map<String,String> createParamMap(HttpServletRequest req){
        Map<String,String> paramMap=new HashMap<>();
        req.getParameterNames().asIterator().forEachRemaining(
                paramName->
                {
                    paramMap.put(paramName,req.getParameter(paramName));
                }
        );

        return paramMap;
    }

    private MyView viewResolver(String viewName){
        return new MyView("/WEB-INF/views/"+viewName+".jsp");
    }
}
