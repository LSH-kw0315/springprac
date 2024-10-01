package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3Adapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4Adapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="frontControllerServletV5",urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String,Object> handlerMappingMap=new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapterList=new ArrayList<>();

    public FrontControllerServletV5(){
        initHanlderMappingMap();
        initHandlerAdapter();

    }
    private void initHanlderMappingMap(){
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form",new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save",new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members",new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form",new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save",new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members",new MemberListControllerV4());
    }
    private void initHandlerAdapter(){
        handlerAdapterList.add(new ControllerV3Adapter());
        handlerAdapterList.add(new ControllerV4Adapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontController");

        Object handler=getHandler(req);
        //컨트롤러(핸들러)를 찾아옴

        if(handler==null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MyHandlerAdapter adapter=getHandlerAdapter(handler);
        //컨트롤러(핸들러)에 맞는 어댑터를 받아옴

        ModelView mv= adapter.handle(req,resp,handler); //어댑터의 handle을 통해 ModelView가 반환된다.
        //실제 req에 넘겨줄 attribute는 handle에서 처리된다.

        viewResolver(mv.getViewName()) //viewResolver로 실제 물리 주소가 매핑된 MyView 반환
                .render(mv.getModel(),req,resp); //MyView의 오버로딩된 render 실행


    }

    private MyView viewResolver(String viewName){
        return new MyView("/WEB-INF/views/"+viewName+".jsp");
    }

    private Object getHandler(HttpServletRequest req){
        String requestURI=req.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler){
        for(MyHandlerAdapter adapter:handlerAdapterList){
            if(adapter.supports(handler)){
                return adapter;
            }
        }

        throw new IllegalArgumentException("적절한 hadler adapter가 없습니다");
    }
}

