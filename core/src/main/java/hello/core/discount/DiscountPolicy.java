package hello.core.discount;

import hello.core.member.Member;

public interface DiscountPolicy {

    //@return : 얼마나 할인을 해줄 것인가?
    int discount(Member member,int price);
}
