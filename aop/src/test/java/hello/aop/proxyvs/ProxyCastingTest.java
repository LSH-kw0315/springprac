package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy(){
        MemberServiceImpl target=new MemberServiceImpl();
        ProxyFactory proxyFactory=new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);

        MemberService memberServiceProxy=(MemberService)proxyFactory.getProxy();
                Assertions.assertThatThrownBy(
                        ()-> {
                    MemberServiceImpl memberServiceImplProxy=
                            (MemberServiceImpl)memberServiceProxy;
                }).isInstanceOf(ClassCastException.class);


    }

    @Test
    void cglibProxy(){
        MemberServiceImpl target=new MemberServiceImpl();
        ProxyFactory proxyFactory=new ProxyFactory(target);

        MemberService memberServiceProxy=(MemberService)proxyFactory.getProxy();

        MemberServiceImpl memberServiceImplProxy=
                            (MemberServiceImpl)memberServiceProxy;



    }
}
