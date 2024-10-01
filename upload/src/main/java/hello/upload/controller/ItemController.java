package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form){
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile=fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles=fileStore.storeFiles(form.getImageFiles());
        
        //DB에 저장하기
        Item item=new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId",item.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model){
        Item item=itemRepository.findById(id);
        model.addAttribute("item",item);
        return "item-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        //file:/... 경로로 파일을 찾아온다. 로컬에서 pdf 열 때 보면 url이 저런 식으로 생기지 않았는가? 그것이다.
        //이렇게만 쓰면 보안에 취약하다. 그냥 아무 주소나 쳐서 이상한 파일이 빠져나올 수도 있다.
        return new UrlResource("file:"+fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName= item.getAttachFile().getUploadFileName();

        UrlResource urlResource=new UrlResource("file:"+fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}",uploadFileName);

        //Body에 집어넣기만 하면 REST API가 반환해주는 것처럼 데이터만 브라우저에 뿌릴 뿐 다운로드는 안된다.
        //return ResponseEntity.ok().body(urlResource);

        //String contentDisposition="attachment; filename=\""+uploadFileName+"\"";
        String encodingDisposition="attachment; filename=\""+ UriUtils.encode(uploadFileName, StandardCharsets.UTF_8)+"\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,encodingDisposition)
                .body(urlResource);
    }





}
