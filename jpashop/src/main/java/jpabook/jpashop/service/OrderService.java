package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        Member member = memberRepository.findOne(memberId);
        Item item= itemRepository.findOne(itemId);

        Delivery delivery=new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();

        //cascade가 존재하기 때문에 order만 저장해도 연관관계가 있는 delivery, orderItem도 엔티티에 저장된다.
        //cascade는 주인만이 참조하는 엔티티인 경우에만 적용하는 것이 좋다. 다른 엔티티도 참조하는 경우 cascade를 막 쓰면 안된다.
    }
    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        Order order=orderRepository.findOne(orderId);
        order.cancel();

        //JPA는 영속성 컨텍스트의 존재 덕에 객체의 데이터를 슉슉 바꿔도 엔티티 매니저가 알아서 더티체크를 해서 업데이트를 날려주므로
        //이렇게 해도 된다.
    }

    //검색

    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }

}
