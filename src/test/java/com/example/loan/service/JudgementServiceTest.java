package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Judgement;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.JudgementDTO;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class JudgementServiceTest {

    @InjectMocks
    private JudgementService judgementService;

    @Mock
    private JudgementRepository judgmentRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;


    @DisplayName("대출심사 등록")
    @Test
    void should_return_newJudgmentEntity() {
        Judgement judgementEntity = Judgement.builder()
                .name("Kim")
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(1L)
                .build();

        JudgementDTO.Request request = JudgementDTO.Request.builder()
                .name("Kim")
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(applicationEntity));
        when(judgmentRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(judgementEntity);

        JudgementDTO.Response actual = judgementService.create(request);

        assertThat(actual.getJudgementId()).isSameAs(judgementEntity.getJudgementId());
        assertThat(actual.getName()).isSameAs(judgementEntity.getName());
        assertThat(actual.getApplicationId()).isSameAs(judgementEntity.getApplicationId());
        assertThat(actual.getApprovalAmount()).isSameAs(judgementEntity.getApprovalAmount());
    }


    @DisplayName("심사 아이디로 조회하기")
    @Test
    void should_return_response_When_RequestExistJudgmentId() {
//        Long findId = 1L;

        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .build();

        when(judgmentRepository.findById(1L)).thenReturn(Optional.of(entity));

        JudgementDTO.Response actual = judgementService.getById(1L);

        assertThat(actual.getJudgementId()).isEqualTo(1L);
    }


    @DisplayName("대출신청에 연결된 심사 조회")
    @Test
    void Should_ReturnResponseOfExistJudgmentEntity_When_RequestExistApplicationId() {
        Long findId = 1L;

        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(applicationEntity));
        when(judgmentRepository.findByApplicationId(findId)).thenReturn(Optional.ofNullable(entity));

        JudgementDTO.Response actual = judgementService.getJudgementOfApplication(findId);

        assertThat(actual.getJudgementId()).isSameAs(findId);
    }


    @DisplayName("대출심사 내용 수정하기")
    @Test
    void should_return_updatedResponse_ifExistsJudgmentId() {
        Long findId = 1L;

        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .name("Kim")
                .build();

        JudgementDTO.Request request = JudgementDTO.Request.builder()
                .name("Lee")
                .build();

        when(judgmentRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));
        when(judgmentRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(entity);

        JudgementDTO.Response actual = judgementService.update(findId, request);

        assertThat(actual.getJudgementId()).isSameAs(findId);
        assertThat(actual.getName()).isSameAs(request.getName());
    }


    @DisplayName("심사 삭제하기")
    @Test
    void should_DeletedJudgmentEntity() {
        Long targetId = 1L;

        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .build();

        when(judgmentRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(entity);
        when(judgmentRepository.findById(targetId)).thenReturn(Optional.ofNullable(entity));

        judgementService.delete(targetId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }


    @DisplayName("대출심사 금액 부여하기")
    @Test
    void should_return_updatedResponse_when_grantApprovalAmountOfJudgmentId() {
        Long findId = 1L;

        Judgement judgementEntity = Judgement.builder()
                .name("Kim")
                .applicationId(findId)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(findId)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        when(judgmentRepository.findById(findId)).thenReturn(Optional.ofNullable(judgementEntity));
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(applicationEntity));
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(applicationEntity);

        ApplicationDTO.GrantAmount actual = judgementService.grant(findId);

        assertThat(actual.getApplicationId()).isSameAs(findId);
        assertThat(actual.getApprovalAmount()).isSameAs(judgementEntity.getApprovalAmount());
    }

}