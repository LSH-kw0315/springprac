package hello.core.beanfind;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

public class ApplicationContextSameBanFindTest {

    AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SameBeanConfig.class);

    @Test
    @DisplayName("타입 조회 시 같은 타입이 둘 이상이면 중복 오류가 나는가?")
    public void findBeanByTypeDuplicate(){
       Assertions.assertThrows(NoUniqueBeanDefinitionException.class,
               ()->ac.getBean(MemberRepository.class));
    }

    @Test
    @DisplayName("타입 조회 시 같은 타입이 둘 이상일 때 해결방법")
    public void findByName(){
        MemberRepository memberRepository=ac.getBean("memberRepository1",MemberRepository.class);

        org.assertj.core.api.Assertions.assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    public void findAllBeanByType(){
        Map<String,MemberRepository> beansOfType=ac.getBeansOfType(MemberRepository.class);
        for(String key:beansOfType.keySet()){
            System.out.println("key="+key+", value="+beansOfType.get(key));
        }

        System.out.println("beansOfType="+beansOfType);

        org.assertj.core.api.Assertions.assertThat(beansOfType.size()).isEqualTo(2);


    }
    //Bean의 이름이 다르지만 반환하는 구현체가 똑같은데, 실무에서도 그럴 수 있다.
    //왜냐면 생성자에 따라 하는 일은 달라질 수 있기 때문이다.
    @Configuration
    static class SameBeanConfig {
        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }
}
