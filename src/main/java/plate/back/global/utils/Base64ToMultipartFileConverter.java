package plate.back.global.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public class Base64ToMultipartFileConverter {

    public static MultipartFile convertBase64ToMultipartFile(String base64String) throws IOException {
        // Decode base64 string to byte array
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // Create a ByteArrayResource from the byte array
        ByteArrayResource resource = new ByteArrayResource(decodedBytes) {
            @Override
            public String getFilename() {
                return "image.jpg"; // Set the filename for the MultipartFile
            }
        };

        // Create a MultipartFile from the ByteArrayResource
        return new CustomMultipartFile(resource);
    }

    // Custom implementation of MultipartFile to handle ByteArrayResource
    private static class CustomMultipartFile implements MultipartFile {

        private final ByteArrayResource resource;

        public CustomMultipartFile(ByteArrayResource resource) {
            this.resource = resource;
        }

        @Override
        public String getName() {
            return resource.getFilename();
        }

        @Override
        public String getOriginalFilename() {
            return resource.getFilename();
        }

        @Override
        public String getContentType() {
            // You may need to set the content type based on your requirements
            return "image/jpeg";
        }

        @Override
        public boolean isEmpty() {
            return resource.contentLength() == 0;
        }

        @Override
        public long getSize() {
            return resource.contentLength();
        }

        @Override
        public byte[] getBytes() throws IOException {
            return resource.getByteArray();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(resource.getByteArray());
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (OutputStream out = new FileOutputStream(dest)) {
                out.write(resource.getByteArray());
            }
        }
    }
}
