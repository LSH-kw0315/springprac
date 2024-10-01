package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConverterTest {

    @Test
    public void stringToInterger(){
        StringToIntegerConverter converter=new StringToIntegerConverter();
        Integer result=converter.convert("10");
        Assertions.assertThat(result).isEqualTo(10);
    }

    @Test
    public void integerToString(){
        IntegerToStringConverter converter=new IntegerToStringConverter();
        String result=converter.convert(10);
        Assertions.assertThat(result).isEqualTo("10");
    }

    @Test
    public void ipPortToString(){
        IpPortToStringConverter converter=new IpPortToStringConverter();
        IpPort ipPort = new IpPort("127.0.0.1", 8080);
        String result=converter.convert(ipPort);
        Assertions.assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    public void stringToIpPort(){
        StringToIpPortConverter converter=new StringToIpPortConverter();
        String str="127.0.0.1:8080";
        IpPort result=converter.convert(str);
        Assertions.assertThat(result).isEqualTo(new IpPort("127.0.0.1",8080));
    }
}
