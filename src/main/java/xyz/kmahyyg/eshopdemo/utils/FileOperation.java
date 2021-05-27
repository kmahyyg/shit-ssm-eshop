package xyz.kmahyyg.eshopdemo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileOperation {
    @Value("${userfile.upload.path}")
    private String uploadFileLocation;

    public void writeFileToDisk(String filename, MultipartFile multipartFile){

    }

    public InputStream readFileFromDisk(String filename) throws IOException {
        return null;
    }
}
