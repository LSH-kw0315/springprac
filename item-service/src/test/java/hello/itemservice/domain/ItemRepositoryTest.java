package hello.itemservice.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    private final ItemRepository itemRepository=new ItemRepository();

    @AfterEach
    public void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    public void save(){
        //given
        Item item=new Item("ItemA",10000,10);
        //when
        Item savedItem=itemRepository.save(item);
        //then
        Item findItem=itemRepository.findById(item.getId());
        Assertions.assertThat(savedItem).isEqualTo(findItem);
    }

    @Test
    public void FindAll(){
        //given
        Item item1=new Item("ItemA",10000,10);
        Item item2=new Item("ItemB",20000,20);

        itemRepository.save(item1);
        itemRepository.save(item2);
        //when
        List<Item> list=itemRepository.findAll();
        //then
        Assertions.assertThat(list.size()).isEqualTo(2);
        Assertions.assertThat(list).contains(item1,item2);
    }

    @Test
    public void UpdateItem(){
        //given
        Item item1=new Item("Item",10000,10);

        Item savedItem=itemRepository.save(item1);
        Long id=savedItem.getId();
        //when
        Item updateItem=new Item("item2",20000,30);
        itemRepository.update(id,updateItem);
        //then
        Item findItem=itemRepository.findById(id);

        Assertions.assertThat(findItem.getItemName()).isEqualTo(updateItem.getItemName());
        Assertions.assertThat(findItem.getPrice()).isEqualTo(updateItem.getPrice());
        Assertions.assertThat(findItem.getQuantity()).isEqualTo(updateItem.getQuantity());
    }

}