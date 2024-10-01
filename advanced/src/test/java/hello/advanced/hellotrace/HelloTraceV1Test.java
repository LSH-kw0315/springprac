package hello.advanced.hellotrace;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import org.junit.jupiter.api.Test;

class HelloTraceV1Test {
    @Test
    void begin_end(){
        HelloTraceV2 trace=new HelloTraceV2();
        TraceStatus status=trace.begin("hello");
        TraceStatus status2=trace.beginSync(status.getTraceId(),"hello");
        trace.end(status2);
        trace.end(status);
    }

    @Test
    void begin_exception(){
        HelloTraceV2 trace=new HelloTraceV2();
        TraceStatus status=trace.begin("exception");
        TraceStatus status2 = trace.beginSync(status.getTraceId(), "exception");
        trace.exception(status2,new IllegalStateException());
        trace.exception(status,new IllegalStateException());

    }
}