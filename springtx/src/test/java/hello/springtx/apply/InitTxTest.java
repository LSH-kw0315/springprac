package hello.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InitTxTest {

    @Autowired Hello hello;

    @Test
    public void go(){
        //초기화 코드는 스프링이 빈 생성을 완료한 시점에 호출되므로, 그냥 비워두기만 하면 된다.
        Ran test=new Ran();
    }

    @TestConfiguration
    static class InitTxTestConfig{

        @Bean
        Hello hello(){
            return new Hello();
        }
    }


    @Slf4j
    static class Ran{

        public Ran(){
            log.info("what is class={}",this.getClass());
        }
    }

    @Slf4j
    static class Hello{
        @PostConstruct
        @Transactional
        public void initV1(){
            boolean isActive= TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx Active={}",isActive);
            log.info("what is class={}",this.getClass());
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean isActive= TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx Active={}",isActive);
            log.info("what is class={}",this.getClass());
        }
    }
}
