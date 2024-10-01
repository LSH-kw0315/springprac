package jpabook.jpashop.repository.order.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count){
        this.orderId=orderId;
        this.orderPrice=orderPrice;
        this.itemName=itemName;
        this.count=count;
    }
}
