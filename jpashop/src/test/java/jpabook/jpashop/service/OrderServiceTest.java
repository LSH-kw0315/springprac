package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() throws Exception{
        //given
        Member member = getMember();

        Book book = getBook("JPA",10000,10);
        int orderCount=2;

        //when
        Long orderId=orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER,order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(10000*orderCount, order.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    private Book getBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","테헤란로","1234"));
        em.persist(member);
        return member;
    }

    @Test
    void 주문취소() throws Exception{
        //given
        Member member=getMember();
        Book book=getBook("JPA",10000,10);
        int orderCount=2;

        Long orderId=orderService.order(member.getId(), book.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);


        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(10, book.getStockQuantity());
    }

    @Test
    void 상품주문_재고수량초과() throws Exception{
        //given
        Member member=getMember();
        Book book=getBook("JPA",10000,10);

        //when, then
        assertThrows(NotEnoughStockException.class,
                ()->orderService.order(member.getId(), book.getId(), 11)
        );

    }
}