package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model) {
        //검증 오류 결과를 errors 맵에 보관
        Map<String, String> errors=new HashMap<>();

        //검증
        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName","상품 이름은 필수입니다.");
        }
        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice()>1000000){
            errors.put("price","가격은 1,000 ~ 1,00,000원까지만 허용됩니다.");
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999){
            errors.put("quantity","수량은 최대 9,999까지 허용됩니다.");
        }

        //복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultPrice=item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                errors.put("globalError","(가격)X(수량)의 값은 10,000원 이상이어야 합니다. 현재 값 = "+resultPrice);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        //tip)읽기 쉬운 코드를 쓰려면 부정의 부정을 조건으로 걸기 보다는 함수로 따로 빼서 읽기 쉽게 하자.
        //아래 경우를 보면 "비어있지 않으면" 이라고 한눈에 알기 어려운데, 함수로 빼서 한눈에 보기 쉽게 하라는 말이다.
        if(!errors.isEmpty()){
            log.info("errors={}",errors);
            model.addAttribute("errors",errors);
            return "validation/v1/addForm";
        }

        //정상 로직인 경우 실행

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

