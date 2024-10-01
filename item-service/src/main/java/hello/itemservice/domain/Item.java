package hello.itemservice.domain;


import lombok.Data;

@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;//null이 될 가능성을 고려
    private Integer quantity;

    public Item(){

    }

    public Item(String itemName, Integer price, Integer quantity){
        this.itemName=itemName;
        this.price=price;
        this.quantity=quantity;
    }
}
