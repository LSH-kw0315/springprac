package hello.advanced.strategy.code.callback;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TimeLogCallbackTest {

    @Test
    void callbackV1(){
        TimeLogCallback template=new TimeLogCallback();
        template.execute(
                new Callback() {
                    @Override
                    public void call() {
                        log.info("비즈니스 로직1 실행");
                    }
                }
        );

        template.execute(
                new Callback() {
                    @Override
                    public void call() {
                        log.info("비즈니스 로직2 실행");
                    }
                }
        );
    }

    @Test
    void callbackV2(){
        TimeLogCallback timeLogCallback=new TimeLogCallback();

        timeLogCallback.execute(() -> log.info("비즈니스 로직1 실행"));
        timeLogCallback.execute(()->log.info("비즈니스 로직2 실행"));
    }

}