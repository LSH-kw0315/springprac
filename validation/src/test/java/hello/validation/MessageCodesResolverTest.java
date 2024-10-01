package hello.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver=new DefaultMessageCodesResolver();

    @Test
    public void MessageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for(String message:messageCodes) {
            System.out.println("messageCode=" + message);
        }

        Assertions.assertThat(messageCodes).containsExactly("required.item","required");
    }

    @Test
    public void messageCodesResolverField(){
        String[] messageCodes=codesResolver.resolveMessageCodes("required","item","itemName",String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        Assertions.assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}

