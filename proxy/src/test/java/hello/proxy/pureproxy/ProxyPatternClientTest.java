package hello.proxy.pureproxy;

import hello.proxy.pureproxy.code.CacheProxy;
import hello.proxy.pureproxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.code.RealSubject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyPatternClientTest {

    @Test
    void noProxyTest(){
        RealSubject realSubject=new RealSubject();
        ProxyPatternClient proxyPatternClient=new ProxyPatternClient(realSubject);

        proxyPatternClient.execute();
        proxyPatternClient.execute();
        proxyPatternClient.execute();

    }

    @Test
    void cacheProxyTest(){
        RealSubject target=new RealSubject();
        CacheProxy cacheProxy=new CacheProxy(target);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
    }

}