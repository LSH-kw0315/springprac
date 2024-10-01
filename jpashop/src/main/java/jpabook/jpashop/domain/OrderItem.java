package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;

//주문과 상품을 잇는 중간 테이블
@Entity
@Getter
@Setter
public class OrderItem {
    protected OrderItem(){}

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY) //상품은 하나지만 그 상품을 주문한 사람은 여러 사람일 수 있다. => 주문상품이 여러 개다.
    @JoinColumn(name="item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch =FetchType.LAZY) //주문은 하나지만 주문된 상품은 여러 개일 수 있으므로
    @JoinColumn(name="order_id") //해당 필드에 대응하는 상대편 테이블 필드
    private Order order;

    private int orderPrice;
    private int count;

    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    //조회로직
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }

    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }
}
