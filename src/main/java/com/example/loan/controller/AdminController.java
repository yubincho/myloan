package com.example.loan.controller;


import com.example.loan.dto.EntryDTO;
import com.example.loan.dto.RepaymentDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.EntryService;
import com.example.loan.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/applications")
public class AdminController extends AbstractController {

    private final EntryService entryService;
    private final RepaymentService repaymentService;


    // 대출 집행 등록
    @PostMapping("{applicationId}/entries")
    public ResponseDTO<EntryDTO.Response> create(@PathVariable Long applicationId, @RequestBody EntryDTO.Request request) {
        return ok(entryService.create(applicationId, request));
    }


    // 대출 집행 조회
//    @GetMapping("/{applicationId}/entries")
//    public ResponseDTO<EntryDTO.Response> get(@PathVariable Long applicationId) {
//        return ok(entryService.get(applicationId));
//    }

    @GetMapping("/{applicationId}/entries")
    public ResponseEntity<EntryDTO.Response> getOne(@PathVariable Long applicationId) {
        EntryDTO.Response response = entryService.get(applicationId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // 대출금 상환
    @PostMapping("{applicationId}/repayments")
    public ResponseDTO<RepaymentDTO.Response> create(@PathVariable Long applicationId, @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.create(applicationId, request));
    }

}
