package com.example.loan.service;

import com.example.loan.domain.Terms;
import com.example.loan.dto.TermsDTO;
import com.example.loan.repository.TermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class TermsServiceTest {

    @InjectMocks
    TermsService termsService;

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;


    @DisplayName("대출 약관 생성하기")
    @Test
    void should_return_newTerms() {

        Terms entity = Terms.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-acc/abcdefg")
                .build();

        TermsDTO.Request request = TermsDTO.Request.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-acc/abcdefg")
                .build();

        when(termsRepository.save(ArgumentMatchers.any(Terms.class))).thenReturn(entity);

        TermsDTO.Response actual = termsService.create(request);

        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getTermsDetailUrl()).isSameAs(entity.getTermsDetailUrl());
    }


    @DisplayName("모든 약관 조회")
    @Test
    void should_return_allTerms_ifExists() {
        Terms entity1 = Terms.builder()
                .name("이용약관 1")
                .termsDetailUrl("https://abc-acdhk/ajsa")
                .build();

        Terms entity2 = Terms.builder()
                .name("이용약관 2")
                .termsDetailUrl("https://abc-acdhk/ajsa/agh")
                .build();

        List<Terms> list = new ArrayList<>(Arrays.asList(entity1, entity2));

        when(termsRepository.findAll()).thenReturn(list);
//        when(termsRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        List<TermsDTO.Response> actusl = termsService.getAll();

        assertThat(actusl.size()).isSameAs(list.size());
    }

}