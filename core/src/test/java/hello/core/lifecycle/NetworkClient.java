package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient  {

    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출."+url);
    }

    public void setUrl(String url){
        this.url=url;
    }

    //서비스 시작 시 호출
    public void connect(){
        System.out.println("connecting..");
    }

    public void call(String message){
        System.out.println("sending..."+message);
    }

    public void disconnect(){
        System.out.println("disconnected.");
    }

    @PostConstruct
    public void init(){
        System.out.println("init");
        connect();
        call("초기화");
    }

    @PreDestroy
    public void close(){
        System.out.println("close");
        disconnect();
    }
}
