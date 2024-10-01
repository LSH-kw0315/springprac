package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV2Spring implements AppInit{
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV2Spring.onStartup");

        AnnotationConfigWebApplicationContext context=new AnnotationConfigWebApplicationContext();
        context.register(HelloConfig.class);

        DispatcherServlet dispatcher=new DispatcherServlet(context);
        servletContext
                .addServlet("dispatcherV2",dispatcher)
                .addMapping("/spring/*");
    }
}
