package com.example.loan.controller;


import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.JudgementDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.JudgementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.loan.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/judgements")
public class JudgementController {

    private final JudgementService judgementService;


    @PostMapping
    public ResponseDTO<JudgementDTO.Response> create(@RequestBody JudgementDTO.Request request) {
        return ok(judgementService.create(request));
    }


    @GetMapping("/{judgmentId}")
    public ResponseDTO<JudgementDTO.Response> get(@PathVariable Long judgmentId) {
        return ok(judgementService.getById(judgmentId));
    }


    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<JudgementDTO.Response> getJudgementOfApplication(@PathVariable Long applicationId) {
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }


    @PostMapping("/{judgementId}")
    public ResponseDTO<JudgementDTO.Response> update(@PathVariable Long judgementId, @RequestBody JudgementDTO.Request request) {
        return ok(judgementService.update(judgementId, request));
    }


    @DeleteMapping("/{judgementId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgementId) {
        judgementService.delete(judgementId);
        return ok();
    }


    @PatchMapping("/{judgementId}/grants")
    public ResponseDTO<ApplicationDTO.GrantAmount> grant(@PathVariable Long judgementId) {
        return ok(judgementService.grant(judgementId));
    }

}
