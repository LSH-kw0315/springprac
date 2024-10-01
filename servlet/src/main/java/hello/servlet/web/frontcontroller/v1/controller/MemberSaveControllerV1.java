package hello.servlet.web.frontcontroller.v1.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberSaveControllerV1 implements ControllerV1{

    private MemberRepository memberRepository=MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username=req.getParameter("username");
        int age=Integer.parseInt(req.getParameter("age"));

        Member member=new Member(username,age);
        memberRepository.save(member);

        req.setAttribute("member",member);

        String viewPath="/WEB-INF/views/save-result.jsp";
        req.getRequestDispatcher(viewPath).forward(req,resp);
    }
}
