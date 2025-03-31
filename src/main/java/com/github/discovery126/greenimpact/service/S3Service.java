package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.config.S3Config;
import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.exception.PhotoNotFoundException;
import com.github.discovery126.greenimpact.exception.TaskNotFoundException;
import com.github.discovery126.greenimpact.repository.TaskCompletionRepository;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final String bucketName;
    private final TaskRepository taskRepository;
    private final TaskCompletionService taskCompletionService;
    private final SecuritySessionContext securitySessionContext;

    public S3Service(S3Client s3Client,
                     S3Config s3Config,
                     SecuritySessionContext securitySessionContext,
                     TaskRepository taskRepository,
                     TaskCompletionService taskCompletionService
    ) {
        this.s3Client = s3Client;
        this.bucketName = s3Config.getBucketName();
        this.securitySessionContext = securitySessionContext;
        this.taskRepository = taskRepository;
        this.taskCompletionService = taskCompletionService;
    }

    public void uploadFile(List<MultipartFile> photos, Long taskId,
                                   CompleteTaskRequest completeTaskRequest) throws IOException {
        if (photos.isEmpty() || (photos.size() == 1 && photos.getFirst().isEmpty())) {
            throw new PhotoNotFoundException("Файлы не загружены");
        }
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Задания с id %d не существует".formatted(taskId));
        }

        int index = 1;
        String myVkCloudUrl = "https://green-impact.hb.ru-msk.vkcloud-storage.ru/";
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : photos) {
            String photoFileName = UUID.randomUUID() + "_" + index;
            String photoFileUrl = "uploads/user/%d/task/%d/%s.jpg".formatted(securitySessionContext.getId(), taskId, photoFileName); ;

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
        taskCompletionService.saveCompletion(completeTaskRequest,
                securitySessionContext.getId(),
                taskId,
                fileUrls);

    }

}
