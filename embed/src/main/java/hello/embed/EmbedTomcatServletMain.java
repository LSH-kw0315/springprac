package hello.embed;

import hello.servlet.HelloServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbedTomcatServletMain {

    public static void main(String[] args) throws LifecycleException {
        System.out.println("EmbedTomcatServletMain.main");

        //톰캣 설정
        Tomcat tomcat=new Tomcat();
        Connector connector=new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        //서블릿 등록
        Context context=tomcat.addContext("","/");

        //특정 환경에서의 오류 잡기
        File docBaseFile = new File(context.getDocBase());
        if(!docBaseFile.isAbsolute()){
            docBaseFile=new File((
                    (org.apache.catalina.Host)context.getParent()).getAppBaseFile(),
                    docBaseFile.getPath()
            );
        }
        docBaseFile.mkdirs();

        //서블릿 등록
        tomcat.addServlet("","helloServlet",new HelloServlet());
        context.addServletMappingDecoded("/hello-servlet","helloServlet");
        tomcat.start();


    }
}
