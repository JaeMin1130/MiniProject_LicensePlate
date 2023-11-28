package plate.back.global.s3.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import plate.back.domain.image.entity.ImageType;

public interface FileService {

    // 파일 업로드
    public Map<String, String> uploadFile(MultipartFile file, int dirIdx) throws IOException;

    // 파일 이동(복사)
    public Map<String, String> moveFile(String imageTitle, ImageType imageType, String answer);

    // 특정 파일 삭제
    public void deleteFile(String imageTitle, ImageType imageType);

    // 전체 삭제
    public void deleteAll(String directory);
}
