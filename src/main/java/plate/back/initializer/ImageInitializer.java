package plate.back.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import plate.back.service.FileService;

@RequiredArgsConstructor
@Component
public class ImageInitializer implements ApplicationRunner {

    private final FileService fileService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // s3 파일 전체 삭제
        fileService.deleteAll("total/vehicle");
        fileService.deleteAll("total/plate");
        fileService.deleteAll("relearn/vehicle");
        fileService.deleteAll("relearn/plate");
    }
}
