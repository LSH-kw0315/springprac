package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void complete() throws NotEnoughMoneyException {
        //given
        Order order=new Order();
        order.setUsername("정상");
        //when
        orderService.order(order);
        //then
        Order findOrder=orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    public void runtimeException() throws NotEnoughMoneyException {
        //given
        Order order=new Order();
        order.setUsername("예외");
        //when
        Assertions.assertThatThrownBy(()->orderService.order(order)).isInstanceOf(RuntimeException.class);
        //then
        Optional<Order> findOrder=orderRepository.findById(order.getId());
        Assertions.assertThat(findOrder.isEmpty()).isTrue();

        //커밋이 되야 SQL을 날린다. 언체크 예외라서 롤백되었고, 따라서 insert가 존재하지 않음.
    }

    @Test
    public void notEnoughMoneyException() throws NotEnoughMoneyException {
        //given
        Order order=new Order();
        order.setUsername("잔고부족");
        //when
        try {
            orderService.order(order);
        }catch (NotEnoughMoneyException e){
            log.info("고객에게 잔고 부족을 알리고 별도 계좌로 입금하도록 안내");
        }
        //then
        Order findOrder=orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("대기");

        //체크 예외이므로 커밋이 발생. insert를 했다!
    }

}