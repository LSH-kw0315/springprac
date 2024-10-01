package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {
    //private final ApplicationContext applicationContext;
    private final ObjectProvider<CallServiceV2> objectProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> objectProvider) {
        this.objectProvider = objectProvider;
    }

    public void external(){
        log.info("call external");
        //applicationContext.getBean(CallServiceV2.class).internal();
        objectProvider.getObject(CallServiceV2.class).internal();
    }

    public void internal(){
        log.info("call internal");
    }

}
