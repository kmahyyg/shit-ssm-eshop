package xyz.kmahyyg.eshopdemo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileOperation {
    @Value("${userfile.upload.path}")
    private String uploadFileLocation;

    public String writeFileToDisk(MultipartFile multipartFile){
        try {
            Files.createDirectories(Paths.get(uploadFileLocation));
            String finalFFName = UUID.randomUUID() + ".png";
            InputStream fis = multipartFile.getInputStream();
            BufferedImage bufimg = ImageIO.read(fis);
            if (bufimg == null) {return null;}
            File outputfile = new File(uploadFileLocation + "/" + finalFFName);
            ImageIO.write(bufimg, "png", outputfile);
            String finalFileHTTPPath = "/api/user/uploadfile/";
            String finalPath = finalFileHTTPPath + finalFFName;
            return finalPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "/static/imgs/error.png";
        }
    }

    public InputStream readFileFromDisk(String filename){
        try {
            String finalFFName = filename;
            int lastSlash = filename.lastIndexOf("/");
            if (lastSlash != -1){finalFFName = filename.substring(lastSlash);}
            File fd = new File(uploadFileLocation + "/" + finalFFName);
            InputStream fis;
            if (fd.exists()) {
                fis = new BufferedInputStream(new FileInputStream(fd));
            } else {
                fis = new BufferedInputStream(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("static/static/imgs/error.png")));
            }
            return fis;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
