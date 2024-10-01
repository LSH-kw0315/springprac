package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryCode {
    private String code; //시스템에서 쓰는 값
    private String displayName; //고객에게 보이는 값
}
