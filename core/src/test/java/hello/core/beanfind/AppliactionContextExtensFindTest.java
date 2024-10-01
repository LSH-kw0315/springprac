package hello.core.beanfind;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

public class AppliactionContextExtensFindTest {
    AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(TestConfig.class);

    @Configuration
    static class TestConfig{
        @Bean
        public DiscountPolicy rateDiscountPolicy(){
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy(){
            return new FixDiscountPolicy();
        }
    }

    @Test
    @DisplayName("부모 타입 조회 시 자식들이 딸려나옴(중복 오류 발생)")
    public void findBeanByParentTypeDuplicate(){
        Assertions.assertThrows(NoUniqueBeanDefinitionException.class,
                ()->ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모 타입 조회 시 자식이 있으면, 빈 이름을 지정")
    public void findBeanByParentTypeBeanName(){
        DiscountPolicy rateDiscountPolicy=ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        org.assertj.core.api.Assertions.assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);

    }

    @Test
    @DisplayName("부모 타입으로 조회하는 대신, 하위 클래스 타입으로 조회")
    public void findBeanBySubClassType(){
        RateDiscountPolicy bean=ac.getBean(RateDiscountPolicy.class);
        org.assertj.core.api.Assertions.assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회")
    public void findBeansByParentType(){
        Map<String,DiscountPolicy> beansOfType=ac.getBeansOfType(DiscountPolicy.class);
        org.assertj.core.api.Assertions.assertThat(beansOfType.size()).isEqualTo(2);
        for(String key:beansOfType.keySet()){
            System.out.println("key="+key+" value="+beansOfType.get(key));
        }
    }

    @Test
    @DisplayName("Object로 조회하기")
    public void findAllBeansByObjectType(){
        Map<String,Object> beansOfType=ac.getBeansOfType(Object.class);
        for(String key:beansOfType.keySet()){
            System.out.println("key="+key+", value="+beansOfType.get(key));
        }
        //모든 객체가 전부 튀어나옴.
    }
}
