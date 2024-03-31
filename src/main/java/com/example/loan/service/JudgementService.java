package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Judgement;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.JudgementDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgementRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;


// 대출 심사 로직
@Service
@RequiredArgsConstructor
public class JudgementService {

    private final JudgementRepository judgementRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;


    public JudgementDTO.Response create(JudgementDTO.Request request) {
        Long applicationId = request.getApplicationId();

        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgement judgment = modelMapper.map(request, Judgement.class);

        Judgement saved = judgementRepository.save(judgment);

        return modelMapper.map(saved, JudgementDTO.Response.class);
    }


    public JudgementDTO.Response getById(Long judgementId) {
        Judgement judgment = judgementRepository.findById(judgementId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(judgment, JudgementDTO.Response.class);
    }


    // 대출신청에 대한 심사 조회
    public JudgementDTO.Response getJudgementOfApplication(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgement judgment = judgementRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(judgment, JudgementDTO.Response.class);
    }


    // 심사 정보 변경(수정)
    public JudgementDTO.Response update(Long judgementId, JudgementDTO.Request request) {
        Judgement judgment = judgementRepository.findById(judgementId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        judgment.setName(request.getName());
        judgment.setApprovalAmount(request.getApprovalAmount());

        judgementRepository.save(judgment);

        return modelMapper.map(judgment, JudgementDTO.Response.class);
    }


    public void delete(Long judgmentId) {
        Judgement judgement = judgementRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        judgement.setIsDeleted(true);

        judgementRepository.save(judgement);
    }


    // ---------------------------->
    // 심사 후 금액 부여
    public ApplicationDTO.GrantAmount grant(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = judgement.getApplicationId();
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal approvalAmount = judgement.getApprovalAmount();
        application.setApprovalAmount(approvalAmount);

        applicationRepository.save(application);

        return modelMapper.map(application, ApplicationDTO.GrantAmount.class);
    }



    // ---------------------------->

    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
