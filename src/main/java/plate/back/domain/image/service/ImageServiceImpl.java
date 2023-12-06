package plate.back.domain.image.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import plate.back.domain.image.entity.Image;
import plate.back.domain.image.entity.ImageType;
import plate.back.domain.image.repository.ImageRepository;
import plate.back.domain.record.entity.Record;
import plate.back.global.s3.service.FileService;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepo;
    private final FileService fileService;

    @Override
    public Image saveVehicleImage(Record record, Map<String, String> vehicleImgMap) {

        return imageRepo.save(Image.builder()
                .record(record)
                .imageUrl(vehicleImgMap.get("url"))
                .imageType(ImageType.VEHICLE)
                .imageTitle(vehicleImgMap.get("title")).build());

    }

    @Override
    public Image savePlateImage(Record record, Map<String, String> plateImgMap) {

        return imageRepo.save(Image.builder()
                .record(record)
                .imageUrl(plateImgMap.get("url"))
                .imageType(ImageType.PLATE)
                .imageTitle(plateImgMap.get("title")).build());
    }

    @Override
    public void updateImage(Record record) {

        List<Image> imgEntityList = imageRepo.findByRecord(record);

        for (Image imageEntity : imgEntityList) {
            String answer = record.getLicensePlate();
            String imageTitle = imageEntity.getImageTitle();
            ImageType imageType = imageEntity.getImageType();

            Map<String, String> map = fileService.moveFile(imageTitle, imageType, answer);

            // AWS S3 파일 삭제
            fileService.deleteFile(imageTitle, imageType);

            imageEntity.updateImage(map.get("title"), map.get("url"));

            imageRepo.save(imageEntity);
        }
        
    }

    @Override
    public void deleteImage(Record record) {

        List<Image> imgEntityList = imageRepo.findByRecord(record);
        
        for (Image imgEntity : imgEntityList) {
            String imageTitle = imgEntity.getImageTitle();
            ImageType imageType = imgEntity.getImageType();
            fileService.deleteFile(imageTitle, imageType);
        }

    }

}
