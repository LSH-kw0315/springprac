package hello.boot;

import hello.spring.HelloConfig;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.util.List;

public class MySpringApplication {
    public static void run(Class configClass, String[] args){
        System.out.println("MySpringApplication.run args="+ List.of(args));

        //톰캣 설정
        Tomcat tomcat=new Tomcat();
        Connector connector=new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        //스프링 컨테이너 설정
        AnnotationConfigWebApplicationContext appContext= new AnnotationConfigWebApplicationContext();
        appContext.register(configClass);

        //디스패처 서블릿을 만들어서 스프링 컨테이너와 연결
        DispatcherServlet dispatcher=new DispatcherServlet(appContext);

        Context context=tomcat.addContext("","/");

        File docBaseFile = new File(context.getDocBase());
        if (!docBaseFile.isAbsolute()) {
            docBaseFile = new File(((org.apache.catalina.Host)
                    context.getParent()).getAppBaseFile(), docBaseFile.getPath());
        }
        docBaseFile.mkdirs();

        tomcat.addServlet("","dispatcher",dispatcher);
        context.addServletMappingDecoded("/","dispatcher");
        try {
            tomcat.start();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
