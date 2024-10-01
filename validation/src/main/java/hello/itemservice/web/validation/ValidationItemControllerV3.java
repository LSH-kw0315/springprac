package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

//    @PostMapping("/add")
//    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//        log.info("objectName={}",bindingResult.getObjectName());
//        log.info("target={}",bindingResult.getTarget());
//
//        if(item.getPrice()!=null && item.getQuantity()!=null){
//            int resultPrice=item.getPrice()*item.getQuantity();
//            if(resultPrice<10000){
//                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
//            }
//        }
//
//        //검증 실패 시 다시 입력 폼으로
//        //tip)읽기 쉬운 코드를 쓰려면 부정의 부정을 조건으로 걸기 보다는 함수로 따로 빼서 읽기 쉽게 하자.
//        //아래 경우를 보면 "비어있지 않으면" 이라고 한눈에 알기 어려운데, 함수로 빼서 한눈에 보기 쉽게 하라는 말이다.
//        if(bindingResult.hasErrors()){
//            log.info("errors={}",bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //정상 로직인 경우 실행
//
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={}",bindingResult.getTarget());

        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultPrice=item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }

        //검증 실패 시 다시 입력 폼으로
        //tip)읽기 쉬운 코드를 쓰려면 부정의 부정을 조건으로 걸기 보다는 함수로 따로 빼서 읽기 쉽게 하자.
        //아래 경우를 보면 "비어있지 않으면" 이라고 한눈에 알기 어려운데, 함수로 빼서 한눈에 보기 쉽게 하라는 말이다.
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v3/addForm";
        }

        //정상 로직인 경우 실행

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

//    @PostMapping("/{itemId}/edit")
//    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {
//
//        if(item.getPrice()!=null && item.getQuantity()!=null){
//            int resultPrice=item.getPrice()*item.getQuantity();
//            if(resultPrice<10000){
//                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
//            }
//        }
//
//        if(bindingResult.hasErrors()){
//            log.info("errors={}",bindingResult);
//            return "validation/v3/editForm";
//        }
//        itemRepository.update(itemId, item);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultPrice=item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v3/editForm";
        }
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

