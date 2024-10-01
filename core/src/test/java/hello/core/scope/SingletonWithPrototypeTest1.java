package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest1 {

    @Test
    public void prototypeFind(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean p1=ac.getBean(PrototypeBean.class);
        p1.addCount();
        Assertions.assertThat(p1.getCount()).isEqualTo(1);

        PrototypeBean p2=ac.getBean(PrototypeBean.class);
        p2.addCount();
        Assertions.assertThat(p2.getCount()).isEqualTo(1);

    }

    @Test
    public void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(ClientBean.class,PrototypeBean.class);
        ClientBean c1=ac.getBean(ClientBean.class);
        ClientBean c2=ac.getBean(ClientBean.class);

        int cnt1=c1.logic();
        Assertions.assertThat(cnt1).isEqualTo(1);

        int cnt2=c2.logic();
        Assertions.assertThat(cnt2).isEqualTo(1);
    }


    @Scope("prototype")
    static class PrototypeBean{
        private int count=0;
        public void addCount(){
            count++;
        }
        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("proto.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("proto.destroy");
        }
    }

    @Scope("singleton")
    static class ClientBean{
        private final PrototypeBean prototypeBean;

//        @Autowired
//        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        @Autowired
        private Provider<PrototypeBean> prototypeBeanObjectProvider;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean){
            this.prototypeBean=prototypeBean;
        }

        public int logic(){
            PrototypeBean prototypeBean=prototypeBeanObjectProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }

        @PostConstruct
        public void init(){
            System.out.println("singleton.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("singleton.destroy");
        }
    }
}
