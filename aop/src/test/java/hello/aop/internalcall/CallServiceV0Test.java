package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(CallLogAspect.class)
@SpringBootTest
@Slf4j
class CallServiceV0Test {

    @Autowired
    CallServiceV0 serviceV0;


    @Test
    void external(){
        log.info("target={}",serviceV0.getClass());
        serviceV0.external();
    }

    @Test
    void internal(){
        serviceV0.internal();
    }
}