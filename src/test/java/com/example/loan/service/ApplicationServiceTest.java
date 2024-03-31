package com.example.loan.service;

import com.example.loan.domain.AcceptTerms;
import com.example.loan.domain.Application;
import com.example.loan.domain.Terms;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.repository.AcceptTermsRepository;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.TermsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    ApplicationService applicationService;

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private TermsRepository termsRepository;
    @Mock
    private AcceptTermsRepository acceptTermsRepository;

    @Spy
    private ModelMapper modelMapper;


    @DisplayName("신규 대출 신청")
    @Test
    void should_return_NewApplication_whenRequestCreateApplication() {
        Application entity =  Application.builder()
                .name("KIM HONG")
                .cellPhone("010-1234-1234")
                .email("hong@gmail.com")
                .hopeAmount(BigDecimal.valueOf(30000000))
                .build();

        ApplicationDTO.Request request = ApplicationDTO.Request.builder()
                .name("KIM HONG")
                .cellPhone("010-1234-1234")
                .email("hong@gmail.com")
                .hopeAmount(BigDecimal.valueOf(30000000))
                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);

        ApplicationDTO.Response actual = applicationService.create(request);

        assertThat(actual.getHopeAmount()).isSameAs(entity.getHopeAmount());
    }


    @DisplayName("대출 신청 조회하기")
    @Test
    void should_return_application_whenFindById() {
        Long findId = 1L;

        Application entity = Application.builder()
                        .applicationId(1L)
                                .build();

        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        ApplicationDTO.Response actual = applicationService.getById(findId);

        assertThat(actual.getApplicationId()).isSameAs(findId);
    }


    @DisplayName("update 하기")
    @Test
    void should_return_updatedResponse_ifExistsId() {
        Long findId = 1L;

        Application entity = Application.builder()
                .applicationId(1L)
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        ApplicationDTO.Request request = ApplicationDTO.Request.builder()
                .hopeAmount(BigDecimal.valueOf(3000000))
                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        ApplicationDTO.Response actual = applicationService.updateById(findId, request);

        assertThat(actual.getApplicationId()).isSameAs(findId);
        assertThat(actual.getHopeAmount()).isNotSameAs(BigDecimal.valueOf(3000000));
    }


    @DisplayName("delete")
    @Test
    void should_delete_ifApplicationIdExists() {
        Long targetId = 1L;

        Application entity = Application.builder()
                        .applicationId(1L)
                                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);
        when(applicationRepository.findById(targetId)).thenReturn(Optional.ofNullable(entity));

        applicationService.deleteById(targetId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }


    @DisplayName("고객이 동의한 약관을 레포지토리에 저장하기")
    @Test
    void Should_AddAcceptTerms_When_RequestAcceptTermsOfApplication() {
        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdddads")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdweqwq")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 2L);

        // 고객이 모든 약관 동의한 것 들어옴
        ApplicationDTO.AcceptTermsDTO request = ApplicationDTO.AcceptTermsDTO.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        when(applicationRepository.findById(findId)).thenReturn(
                Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(Arrays.asList(entityA, entityB));
        when(acceptTermsRepository.save(ArgumentMatchers.any(AcceptTerms.class))).thenReturn(AcceptTerms.builder().build());


        Boolean actual = applicationService.acceptTerms(findId, request);
        assertThat(actual).isTrue();
    }

    @DisplayName("고객이 모든 약관 동의하지 않을때 예외처리")
    @Test
    void Should_ThrowException_When_RequestNotAllAcceptTerms() {
        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdddads")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdweqwq")
                .build();

        // 고객이 1개만 동의
        List<Long> acceptTerms = Arrays.asList(1L);
        ApplicationDTO.AcceptTermsDTO request = ApplicationDTO.AcceptTermsDTO.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        when(applicationRepository.findById(findId)).thenReturn(
                Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .thenReturn(Arrays.asList(entityA, entityB));

        Assertions.assertThrows(BaseException.class, () -> applicationService.acceptTerms(1L, request));
    }

    @DisplayName("(고객이) 존재하지 않은 약관을 동의할 때 예외처리")
    @Test
    void Should_ThrowException_When_RequestNotExistAcceptTermsOfApplication() {
        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdddads")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdweqwq")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 3L);

        ApplicationDTO.AcceptTermsDTO request = ApplicationDTO.AcceptTermsDTO.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        when(applicationRepository.findById(findId)).thenReturn(
                Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(Arrays.asList(entityA, entityB));

        Assertions.assertThrows(BaseException.class, () -> applicationService.acceptTerms(1L, request));
    }

}