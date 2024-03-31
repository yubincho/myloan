package com.example.loan.controller;

import com.example.loan.dto.CounselDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.loan.dto.ResponseDTO.ok;


// 대출 상담 관련 CRUD
@RestController
@RequiredArgsConstructor
@RequestMapping("/counsels")
public class CounselController {

    private final CounselService counselService;

    @PostMapping()
    public ResponseDTO<CounselDTO.Response> create(@RequestBody CounselDTO.Request request) {
        return ok(counselService.create(request));
    }


    @GetMapping("/{counselId}")
    public ResponseDTO<CounselDTO.Response> getById(@PathVariable Long counselId) {
        return ok(counselService.getById(counselId));
    }

    @PostMapping("/{counselId}")
    public ResponseDTO<CounselDTO.Response> update(
            @PathVariable Long counselId, @RequestBody CounselDTO.Request request) {
        return ok(counselService.updateById(counselId, request));
    }


    @DeleteMapping("/{counselId}")
    public ResponseDTO<CounselDTO.Response> delete(@PathVariable Long counselId) {
        counselService.deleteById(counselId);
        return ok();
    }



}
