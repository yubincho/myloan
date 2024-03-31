package com.example.loan.service;


import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final ApplicationRepository applicationRepository;


    // 단순 업로드 되는지 확인
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get(uploadPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }


    // 파일 다운로드
    public Resource load(String filename) {

        try {
            Path file = Paths.get(uploadPath).resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.isReadable() || resource.exists()) {
                return resource;
            } else {
                throw new BaseException(ResultType.SYSTEM_ERROR);
            }
        } catch (MalformedURLException e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

    }


    public Stream<Path> loadAll() {

        try {
            return Files.walk(Paths.get(uploadPath), 1).filter(path -> !path.equals(Paths.get(uploadPath)));
        } catch (IOException e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }


    public void deleteAll() {

        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
    }



}
