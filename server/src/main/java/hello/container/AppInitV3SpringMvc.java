package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV3SpringMvc implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");
        AnnotationConfigWebApplicationContext context=new AnnotationConfigWebApplicationContext();
        context.register(HelloConfig.class);

        DispatcherServlet dispatcher=new DispatcherServlet(context);
        servletContext
                .addServlet("dispatcherV3",dispatcher)
                .addMapping("/");

    }
}
