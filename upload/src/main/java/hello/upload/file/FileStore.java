package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir+filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult=new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename=multipartFile.getOriginalFilename();

        String storeName=createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeName)));

        return new UploadFile(originalFilename,storeName);
    }

    private String createStoreFileName(String originalFilename) {
        String extension=extractExtension(originalFilename);

        //서버에 저장할 이름을 생성
        String uuid = UUID.randomUUID().toString();
        return uuid+"."+extension;
    }

    private String extractExtension(String originalFilename){
        int pos=originalFilename.indexOf(".");
        return originalFilename.substring(pos+1);
    }

}
