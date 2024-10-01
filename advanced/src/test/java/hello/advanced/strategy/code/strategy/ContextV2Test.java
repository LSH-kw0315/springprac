package hello.advanced.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ContextV2Test {
    @Test
    void strategyV1(){
        ContextV2 context1=new ContextV2();
        context1.execute(new StrategyLogic1());
        context1.execute(new StrategyLogic2());
    }

    @Test
    void strategyV2(){
        ContextV2 context1=new ContextV2();
        context1.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context1.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    @Test
    void strategyV3(){
        ContextV2 context1=new ContextV2();
        context1.execute(()->
                log.info("비즈니스 로직1 실행")
        );
        context1.execute(()->
                log.info("비즈니스 로직2 실행"));
    }
}