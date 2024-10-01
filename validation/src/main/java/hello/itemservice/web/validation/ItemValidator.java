package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Item.class.isAssignableFrom(aClass);
        //쉽게 말해 aClass가 Item 클래스의 SubClass인가를 따진다.
        //javascript의 instaceof를 떠올리면 된다.
    }

    @Override
    public void validate(Object o, Errors errors) {
        Item item=(Item) o;
        if(!StringUtils.hasText(item.getItemName())){
            errors.rejectValue("itemName","required");

        }
        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice()>1000000){
            errors.rejectValue("price","range",new Object[]{1000,1000000},null);
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999){
            errors.rejectValue("quantity","max",new Object[]{9999},null);
        }

        //복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultPrice=item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                errors.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }

    }
}
