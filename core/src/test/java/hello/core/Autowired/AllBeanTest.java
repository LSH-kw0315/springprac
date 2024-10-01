package hello.core.Autowired;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

public class AllBeanTest {

    @Test
    public void findAllBean(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(AutoAppConfig.class,DiscountService.class);

        DiscountService ds=ac.getBean(DiscountService.class);
        Member m=new Member(1L,"userA", Grade.VIP);
        int discountPrice=ds.discount(m,10000,"fixDiscountPolicy");

        Assertions.assertThat(ds).isInstanceOf(DiscountService.class);
        Assertions.assertThat(discountPrice).isEqualTo(1000);

        discountPrice=ds.discount(m,20000,"rateDiscountPolicy");
        Assertions.assertThat(ds).isInstanceOf(DiscountService.class);
        Assertions.assertThat(discountPrice).isEqualTo(2000);
    }

    static class DiscountService{
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policyList;

        @Autowired
        public DiscountService(Map<String,DiscountPolicy> policyMap,List<DiscountPolicy> policyList){
            this.policyList=policyList;
            this.policyMap=policyMap;
            System.out.println("policyMap:"+policyMap);
            System.out.println("policyList:"+policyList);
        }

        public int discount(Member member,int price,String discountPolicy){
            DiscountPolicy ds=policyMap.get(discountPolicy);
            return ds.discount(member,price);
        }
    }
}
