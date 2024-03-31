package com.example.loan.service;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.CounselRepository;
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

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class CounselServiceTest {

    @InjectMocks
    CounselService counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;


    @DisplayName("create 상담하기")
    @Test
    void should_return_Response() {
        Counsel entity = Counsel.builder()
                .name("Kim")
                .cellPhone("010-1234-1234")
                .email("kim@gmail.com")
                .memo("대출 원함")
                .zipCode("12345")
                .address("서울특별시 강남구 대치동")
                .addressDetail("101-1001")
                .build();

        CounselDTO.Request request = CounselDTO.Request.builder()
                .name("Kim")
                .cellPhone("010-1234-1234")
                .email("kim@gmail.com")
                .memo("대출 원함")
                .zipCode("12345")
                .address("서울특별시 강남구 대치동")
                .addressDetail("101-1001")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);

        CounselDTO.Response actual = counselService.create(request);

        assertThat(actual.getName()).isSameAs(entity.getName());
    }


    @DisplayName("id로 찾기")
    @Test
    void should_return_responseIfExistsId() {
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        CounselDTO.Response actual = counselService.getById(findId);

        assertThat(actual.getCounselId()).isSameAs(findId);
    }

    @DisplayName("id가 없으면 예외처리")
    @Test
    void should_exception_ifIdNotExists() {
        Long findId = 2L;

        when(counselRepository.findById(findId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        Assertions.assertThrows(BaseException.class, () -> counselService.getById(findId));
    }

    @DisplayName("id찾아서 update하기")
    @Test
    void should_return_updateIfIdExists() {
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .name("Jimmm")
                .build();


        CounselDTO.Request request = CounselDTO.Request.builder()
                .name("Kang")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        CounselDTO.Response actual = counselService.updateById(findId, request);

        assertThat(actual.getCounselId()).isSameAs(findId);
        assertThat(actual.getName()).isSameAs("Kang");
    }


    @Test
    void should_deletedCounselEntity_whenDeleted() {
        Long targetId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        when(counselRepository.findById(targetId)).thenReturn(Optional.ofNullable(entity));

        counselService.deleteById(targetId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

}