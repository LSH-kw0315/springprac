package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        public void dbInit1(){
            Member member = createMember("userA","서울","1","111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK",10000,100);
            em.persist(book1);

            Book book2=createBook("JPA2 BOOK",20000,100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order=Order.createOrder(member, delivery,orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = createMember("userB","경기","2","222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK",20000,200);
            em.persist(book1);

            Book book2=createBook("SPRING2 BOOK",40000,300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order=Order.createOrder(member, delivery,orderItem1, orderItem2);
            em.persist(order);
        }

        private Book createBook(String name, int price, int quantity) {
            Book book=new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(quantity);
            return book;
        }

        private Member createMember(String name,String city, String street, String zipcode) {
            Member member=new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }


    }
}


