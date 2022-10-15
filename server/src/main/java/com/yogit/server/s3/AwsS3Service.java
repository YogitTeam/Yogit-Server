package com.yogit.server.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yogit.server.global.exception.FailedUploadImageS3ContainerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwsS3Service{

    //    @Value("${cloud.aws.s3.bucket}")
    private static String bucket = "yogit";

    //    @Value("${cloud.aws.region.static}")
    private static String region = "ap-northeast-2";

    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile file){
        return getFilenameAndPutS3(file);
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles){
        return multipartFiles
                .stream()
                .map(this::uploadImage)
                .collect(Collectors.toList());
    }

    private String getFilenameAndPutS3(MultipartFile file){
        String filename = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new FailedUploadImageS3ContainerException();
        }
        return filename;
    }

    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName){
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    public static String makeUrlOfFilename(String filename){
        return "https://"+bucket+".s3."+region+".amazonaws.com/"+filename;
    }

    public static List<String> makeUrlsOfFilenames(List<String> filenames){
        ArrayList<String> imageUrls = new ArrayList<>();
        for (String filename : filenames) imageUrls.add("https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename);
        return imageUrls;
    }

    public static List<String> makeUrlsOfCommaSplitFilenames(String filenamesStr){
        String[] filenames = filenamesStr.split(",");
        ArrayList<String> imageUrls = new ArrayList<>();
        for (String filename : filenames) imageUrls.add(makeUrlOfFilename(filename));
        return imageUrls;
    }
}
