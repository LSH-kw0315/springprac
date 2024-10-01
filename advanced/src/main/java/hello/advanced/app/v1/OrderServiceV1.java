package hello.advanced.app.v1;

import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

    public void OrderItem(String itemId){
        TraceStatus status=null;
        try {
            status=trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status,e);
            throw e; //예외를 처리해서 정상 흐름이 되면 안되므로
        }
    }
}
