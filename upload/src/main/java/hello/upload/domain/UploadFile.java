package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName; //업로드 시의 이름
    private String storeFileName; //실제 서버에 저장될 이름. 업로드 될 파일 이름은 중복될 가능성이 매우 높음

    public UploadFile(String uploadFileName,String storeFileName){
        this.storeFileName=storeFileName;
        this.uploadFileName=uploadFileName;
    }
}
