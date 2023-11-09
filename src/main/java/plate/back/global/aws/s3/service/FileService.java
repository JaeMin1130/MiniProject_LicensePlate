package plate.back.global.aws.s3.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final String[] directories = { "total/vehicle/", "total/plate/", "relearn/vehicle/", "relearn/plate/" };
    private final AmazonS3 amazonS3;

    // 파일 업로드
    public String[] uploadFile(MultipartFile file, int dirIdx) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        PutObjectResult result = amazonS3.putObject(
                new PutObjectRequest(bucket, directories[dirIdx] + fileName, file.getInputStream(), objectMetadata)
        // .withCannedAcl(CannedAccessControlList.PublicRead)); // 누구나 파일 읽기 가능
        );
        String url = amazonS3.getUrl(bucket, directories[dirIdx] + fileName).toString();
        return new String[] { url, fileName };
    }

    // 파일 이동(복사)
    public Map<String, String> moveFile(String imageTitle, String imageType, String answer) {
        String[] imgTitleArr = imageTitle.split("\\.");
        String answerTitle = String.format("%sans%s.%s", imgTitleArr[0], answer,
                imgTitleArr[1]);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket,
                "total/" + imageType + "/" + imageTitle, bucket,
                "relearn/" + imageType + "/" + answerTitle);

        amazonS3.copyObject(copyObjectRequest);

        Map<String, String> result = new HashMap<>();
        String url = amazonS3.getUrl(bucket, "relearn/" + imageType + "/" + answerTitle).toString();
        result.put("url", url);
        result.put("title", answerTitle);

        return result;
    }

    // 특정 파일 삭제
    public void deleteFile(String imageTitle, String imageType) {

        List<KeyVersion> keysToDelete = new ArrayList<>();

        keysToDelete.add(new KeyVersion("total/" + imageType + "/" + imageTitle));
        keysToDelete.add(new KeyVersion("relearn/" + imageType + "/" + imageTitle));

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket)
                .withKeys(keysToDelete);

        DeleteObjectsResult delObjRes = amazonS3.deleteObjects(deleteObjectsRequest);
        int successfulDeletes = delObjRes.getDeletedObjects().size();
        System.out.println(successfulDeletes + " objects successfully deleted.");
    }

    // 전체 삭제
    public void deleteAll(String directory) {
        ObjectListing objectListing = amazonS3.listObjects(bucket, directory);

        // Iterate through the objects in the directory and delete them
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            amazonS3.deleteObject(bucket, objectSummary.getKey());
        }
    }
}