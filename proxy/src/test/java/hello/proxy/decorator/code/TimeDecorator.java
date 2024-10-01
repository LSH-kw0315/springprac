package hello.proxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component{

    private Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
       log.info("타임 데코 실행");

       long startTime=System.currentTimeMillis();
       String result=component.operation();
       long endTime=System.currentTimeMillis();
       long resultTime=endTime-startTime;
       log.info("타임 데코 종료. 결과 시간={}ms",resultTime);
       return result;
    }
}
