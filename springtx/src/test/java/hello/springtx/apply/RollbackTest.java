package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollbackTest {
    @Autowired RollbackService rollbackService;

    @Test
    public void runtimeException(){
        Assertions.assertThatThrownBy(()->        rollbackService.runtimeException()).isInstanceOf(RuntimeException.class);

    }

    @Test
    public void uncheckedException() throws MyException {
        Assertions.assertThatThrownBy(()->        rollbackService.checkedException()).isInstanceOf(MyException.class);

    }

    @Test
    public void rollbackForException() throws MyException {
        Assertions.assertThatThrownBy(()->        rollbackService.rollbackFor()).isInstanceOf(MyException.class);

    }


    @TestConfiguration
    static class RollbackTestConfig{
        @Bean
        public RollbackService rollbackService(){
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService{

        //런타임 예외(언체크)
        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new RuntimeException();
        }


        //체크예외
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }
        //체크 예외를 rollbackError 옵션으로 지정
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }
    }

    static class MyException extends Exception{

    }

}
