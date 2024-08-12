package tasteMap.backend.global.S3.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Uploader {

    void uploadCourse(MultipartFile file, Long id);
    void uploadRoot(List<MultipartFile> files, Long id);
    void deleteCourse(Long courseId);
    void deleteRoot(Long courseId);

}
