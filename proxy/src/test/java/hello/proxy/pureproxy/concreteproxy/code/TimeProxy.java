package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{
    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation() {
        log.info("타임 데코 실행");

        long startTime=System.currentTimeMillis();
        String result=concreteLogic.operation();
        long endTime=System.currentTimeMillis();
        long resultTime=endTime-startTime;
        log.info("타임 데코 종료. 결과 시간={}ms",resultTime);
        return result;
    }
}
