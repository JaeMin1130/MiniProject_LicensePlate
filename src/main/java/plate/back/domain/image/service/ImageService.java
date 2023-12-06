package plate.back.domain.image.service;

import java.util.Map;

import plate.back.domain.image.entity.Image;
import plate.back.domain.record.entity.Record;

public interface ImageService {
    
    public Image saveVehicleImage(Record record, Map<String, String> vehicleImgMap);

    public Image savePlateImage(Record record, Map<String, String> plateImgMap);

    public void updateImage(Record record);

    public void deleteImage(Record record);
}   
