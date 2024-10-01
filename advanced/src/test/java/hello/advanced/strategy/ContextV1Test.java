package hello.advanced.strategy;

import hello.advanced.strategy.code.strategy.ContextV1;
import hello.advanced.strategy.code.strategy.Strategy;
import hello.advanced.strategy.code.strategy.StrategyLogic1;
import hello.advanced.strategy.code.strategy.StrategyLogic2;
import hello.advanced.template.code.AbstractTemplate;
import hello.advanced.template.code.SubClassLogic1;
import hello.advanced.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {
    @Test
    void strategyV0(){
        logic1();
        logic2();
    }

    private void logic1(){
        long startTime=System.currentTimeMillis();

        log.info("비즈니스 로직1 실행");

        long endTime=System.currentTimeMillis();
        long resultTime=endTime-startTime;
        log.info("resultTime={}",resultTime);
    }

    private void logic2(){
        long startTime=System.currentTimeMillis();

        log.info("비즈니스 로직2 실행");

        long endTime=System.currentTimeMillis();
        long resultTime=endTime-startTime;
        log.info("resultTime={}",resultTime);
    }

    @Test
    void strategyV1(){
        Strategy strategy1 = new StrategyLogic1();
        ContextV1 context1=new ContextV1(strategy1);
        context1.execute();;

        Strategy strategy = new StrategyLogic2();
        ContextV1 context2=new ContextV1(strategy);
        context2.execute();;


    }

    @Test
    void strategyV2(){
        Strategy strategy1 =
                new Strategy() {
                    @Override
                    public void call() {
                        log.info("비즈니스 로직1 실행");
                    }
                };

        ContextV1 context1 = new ContextV1(strategy1);
        context1.execute();

        Strategy strategy2 =
                new Strategy() {
                    @Override
                    public void call() {
                        log.info("비즈니스 로직2 실행");
                    }
                };

        ContextV1 context2 = new ContextV1(strategy2);
        context2.execute();


    }

    @Test
    void strategyV3(){

        ContextV1 context1 = new ContextV1(               new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context1.execute();


        ContextV1 context2 = new ContextV1(                new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
        context2.execute();

    }

    @Test
    void strategyV4(){

        ContextV1 context1 = new ContextV1(()->log.info("비즈니스 로직1"));
        context1.execute();


        ContextV1 context2 = new ContextV1(()->log.info("비즈니스 로직2"));
        context2.execute();

    }

}
