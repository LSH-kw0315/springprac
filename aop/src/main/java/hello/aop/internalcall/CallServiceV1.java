package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    public void external(){
        log.info("call external");
        callServiceV1.internal();
    }

    public void internal(){
        log.info("call internal");
    }

    @Autowired
    public void setCallService(CallServiceV1 callServiceV1){
        //스프링에 의해 프록시를 주입받게 될 것이다.
        this.callServiceV1=callServiceV1;
    }
}
