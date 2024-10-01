package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 DI 컨테이너")
    public void pureContainer(){
        AppConfig appConfig=new AppConfig();

        MemberService memberService1= appConfig.memberService();
        MemberService memberService2= appConfig.memberService();

        System.out.println("memberService1:"+memberService1);
        System.out.println("memberService2:"+memberService2);

        Assertions.assertThat(memberService1).isNotEqualTo(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴")
    public void singletonServiceTest(){
        SingletonService singletonService1=SingletonService.getInstance();
        SingletonService singletonService2=SingletonService.getInstance();

        Assertions.assertThat(singletonService1).isSameAs(singletonService2);

        //isSameAs : ==
        //isEqualTo : equals()
    }

    @Test
    @DisplayName("스프링 컨테이너")
    public void springContainer(){
        AnnotationConfigApplicationContext as=new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1=as.getBean("memberService", MemberService.class);
        MemberService memberService2=as.getBean("memberService",MemberService.class);

        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
}
