package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    public void statefulServiceSingleton(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1=ac.getBean(StatefulService.class);
        StatefulService statefulService2=ac.getBean(StatefulService.class);

        int userAPrice=statefulService1.order("userA",10000);
        int userBPrice=statefulService2.order("userB",20000);

        System.out.println("price="+userAPrice);
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }

}