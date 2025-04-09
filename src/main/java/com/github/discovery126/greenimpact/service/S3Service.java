package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.config.S3Config;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final String bucketName;
    private final TaskRepository taskRepository;
    private final TaskUserService taskUserService;
    private final SecuritySessionContext securitySessionContext;

    public S3Service(S3Client s3Client,
                     S3Config s3Config,
                     SecuritySessionContext securitySessionContext,
                     TaskRepository taskRepository,
                     TaskUserService taskUserService
    ) {
        this.s3Client = s3Client;
        this.bucketName = s3Config.getBucketName();
        this.securitySessionContext = securitySessionContext;
        this.taskRepository = taskRepository;
        this.taskUserService = taskUserService;
    }

    public void uploadFile(List<MultipartFile> photos, Long taskId,
                                   String comment) throws IOException {
        if (photos.isEmpty() || (photos.size() == 1 && photos.getFirst().isEmpty())) {
            throw new CustomException(ValidationConstants.PHOTO_NOT_UPLOAD);
        }
        if (!taskRepository.existsById(taskId)) {
            throw new CustomException(ValidationConstants.TASK_ID_NOT_FOUND);
        }

        int index = 1;
        String myVkCloudUrl = "https://green-impact.hb.ru-msk.vkcloud-storage.ru/";
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : photos) {
            String photoFileName = UUID.randomUUID() + "_" + index;
            String dateNowString = LocalDate.now().toString();
            String photoFileUrl = "uploads/user/%d/task/%d/%s/%s.jpg"
                    .formatted(securitySessionContext.getId(), taskId, dateNowString, photoFileName); ;

            Path tempFile = Files.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .key(photoFileUrl)
                    .build();

            s3Client.putObject(request, tempFile);
            fileUrls.add(myVkCloudUrl + photoFileUrl);
            index += 1;
        }
        taskUserService.saveCompletion(comment,
                taskId,
                fileUrls);

    }

}
