package hellojpa.domain;

import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id @GeneratedValue
    @Column(name="ORDER_ITEM_ID")
    private Long id;

//    @Column(name="ORDER_ID")
//    private Long orderId;
//
//    @Column(name="ITEM_ID")
//    private Long itemId;

    @ManyToOne //주문 하나에는 여러 아이템이 존재함
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne //한 아이템은 여러 주문에 속해있을 수 있음
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    private int orderPrice;
    private int count;

    public void setId(Long id) {
        this.id = id;
    }

//    public void setOrderId(Long orderId) {
//        this.orderId = orderId;
//    }
//
//    public void setItemId(Long itemId) {
//        this.itemId = itemId;
//    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

//    public Long getOrderId() {
//        return orderId;
//    }
//
//    public Long getItemId() {
//        return itemId;
//    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public int getCount() {
        return count;
    }
}
