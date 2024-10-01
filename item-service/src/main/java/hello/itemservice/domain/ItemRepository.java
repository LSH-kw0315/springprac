package hello.itemservice.domain;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    //실무에서 Map을 사용하고 싶으면 ConcurrentHashMap을 사용해야 한다. 동시성 문제 때문.
    private static final Map<Long,Item> store=new HashMap<>();
    private static long sequence=0L;

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(),item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long id, Item updateItem){
        Item findItem=findById(id);
        findItem.setItemName(updateItem.getItemName());
        findItem.setPrice(updateItem.getPrice());
        findItem.setQuantity(updateItem.getQuantity());

        //보면 updateItem의 id는 쓰지 않는다. 이런 경우, Item의 정보를 다루는 DTO를 사용하는 것이 더 바람직하다.
    }
    public void clearStore(){
        store.clear();
    }
}
