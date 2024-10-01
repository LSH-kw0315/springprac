package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonBeanTest {

    @Test
    public void singletonBeanFind(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SingletonBean.class);

        SingletonBean s1=ac.getBean(SingletonBean.class);
        SingletonBean s2=ac.getBean(SingletonBean.class);

        Assertions.assertThat(s1).isSameAs(s2);

        ac.close();
    }

    @Scope("singleton")
    static class SingletonBean{
        @PostConstruct
        public void init(){
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("SingletonBean.destroy");
        }
    }
}
