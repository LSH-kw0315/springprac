package hello.servlet.basic.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body=json")
public class RequestBodyJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream=req.getInputStream();
        String body= StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("body:"+body);

        HelloData helloData=objectMapper.readValue(body, HelloData.class);

        System.out.println("username:"+helloData.getUsername());
        System.out.println("age:"+helloData.getAge());

        resp.getWriter().write("ok");
    }
}
