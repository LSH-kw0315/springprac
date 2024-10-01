package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    DiscountPolicy discountPolicy=new RateDiscountPolicy();

    @Test
    @DisplayName("VIP 10% 할인")
    void vip_o(){
        //given
        Member member=new Member(1L,"memberVIP", Grade.VIP);
        //when
        int discount=discountPolicy.discount(member,10000);
        Assertions.assertThat(discount).isEqualTo(1000);

    }

    @Test
    @DisplayName("Basic no sale")
    void vip_x(){
        Member member=new Member(2L,"memberBasic",Grade.BASIC);
        int discount=discountPolicy.discount(member,10000);
        Assertions.assertThat(discount).isEqualTo(1000);
    }

}