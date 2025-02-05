package hello.jdbc.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    static class MyCheckedException extends Exception{
        public MyCheckedException(String message){
            super(message);
        }
    }

    static class Service{
        Repository repository=new Repository();

        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message={}",e.getMessage());
            }
        }

        public void callThrow() throws MyCheckedException {
            repository.call();
        }

    }

    static class Repository{
        public void call() throws MyCheckedException {
            throw new MyCheckedException("sex");
        }
    }

    @Test
    public void checked_catch(){
        Service service=new Service();
        service.callCatch();
    }

    @Test
    public void checked_throw() throws MyCheckedException {
        Service service=new Service();

        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }

}
