package com.example.loan.controller;


import com.example.loan.domain.Application;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.FileDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.ApplicationService;
import com.example.loan.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController extends  AbstractController {

    private final ApplicationService applicationService;
    private final FileStorageService fileStorageService;


    @PostMapping()
    public ResponseDTO<ApplicationDTO.Response> create(@RequestBody ApplicationDTO.Request request) {
        return ok(applicationService.create(request));
    }

//    @PostMapping("/applications")
//    public ResponseEntity<ApplicationDTO.Response> creates(@RequestBody ApplicationDTO.Request request) {
//        ApplicationDTO.Response response = applicationService.create(request);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationDTO.Response> getById(@PathVariable Long applicationId) {
        return ok(applicationService.getById(applicationId));
    }


    @PostMapping("/{applicationId}")
    public ResponseDTO<ApplicationDTO.Response> updateById(
            @PathVariable Long applicationId, @RequestBody ApplicationDTO.Request request) {
        return ok(applicationService.updateById(applicationId, request));
    }

//    @PostMapping("/{applicationId}")
//    public ResponseEntity<ApplicationDTO.Response> update(
//            @PathVariable Long applicationId, @RequestBody ApplicationDTO.Request request) {
//        ApplicationDTO.Response response = applicationService.updateById(applicationId, request);
//        return ResponseEntity.ok(response);
//    }


    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
        applicationService.deleteById(applicationId);

        return ok();
    }

    // -------------------------------------------------->
    // 대출 신청 - (동의한) 약관 등록
    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Boolean> acceptTerms(@PathVariable Long applicationId, @RequestBody ApplicationDTO.AcceptTermsDTO request) {
        return ok(applicationService.acceptTerms(applicationId, request));
    }


    // -------------------------------------------------->
    // 파일 업로드 확인 - simple
    @PostMapping("/files")
    public ResponseDTO<Void> upload(MultipartFile file) {
        fileStorageService.save(file);
        return ok();
    }


    // 파일 다운로드
    @GetMapping("/files")
    public ResponseEntity<Resource> download(@RequestParam(value = "filename") String filename) {
        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }


    // 파일 저장한 경로의 모든 파일 조회(업로드, 다운로드 모두)
    @GetMapping("/files/info")
    public ResponseDTO<List<FileDTO>> getFiles() {
        List<FileDTO> fileInfos = fileStorageService.loadAll().map(path -> {
            String fileName= path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class, "download", fileName).build().toString()).build();
        }).collect(Collectors.toList());

        return ok(fileInfos);
    }


    // 모든 파일 삭제
    @DeleteMapping("/files")
    public ResponseDTO<Void> deleteAll() {
        fileStorageService.deleteAll();
        return ok();
    }

}
