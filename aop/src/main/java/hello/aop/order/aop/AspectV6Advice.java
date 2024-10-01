package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    @Around("hello.aop.order.aop.Pointcuts.allOrder() && hello.aop.order.aop.Pointcuts.allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws  Throwable{
        try{
            log.info("[트랜잭션 시작] {}",joinPoint.getSignature());
            Object result=joinPoint.proceed();
            log.info("[트랜잭션 커밋] {}",joinPoint.getSignature());
            return result;
        }catch (Exception e){
            log.info("[트랜잭션 롤백] {}",joinPoint.getSignature());
            throw e;
        }finally {
            log.info("[리소스 릴리즈] {}",joinPoint.getSignature());
        }
    }

    @Before("hello.aop.order.aop.Pointcuts.allService()")
    public void doBefore(JoinPoint joinPoint){
        log.info("[before] {}",joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()",returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result){
        log.info("[return] {} return={}",joinPoint.getSignature(),result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()",throwing = "ex")
    public void doThrow(JoinPoint joinPoint, Exception ex){
        log.info("[ex] {} message={}",joinPoint.getSignature(),ex);
    }

    @After(value="hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[after] {}",joinPoint.getSignature());
    }


}
