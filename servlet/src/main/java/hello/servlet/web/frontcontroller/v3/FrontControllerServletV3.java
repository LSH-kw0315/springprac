package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name="frontControllerServletV3",urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap=new HashMap<>();

    public FrontControllerServletV3(){
        controllerMap.put("/front-controller/v3/members/new-form",new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save",new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members",new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontController");

        String requestURI=req.getRequestURI();

        ControllerV3 controller=controllerMap.get(requestURI);

        if(controller==null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Map<String,String> paramMap=createParamMap(req);


        ModelView mv=controller.process(paramMap); //process를 통해 ModelView가 반환된다.

        viewResolver(mv.getViewName()) //viewResolver로 실제 물리 주소가 매핑된 MyView 반환
                .render(mv.getModel(),req,resp); //MyView의 오버로딩된 render 실행

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
