package com.example.loan.service;

import com.example.loan.domain.Terms;
import com.example.loan.dto.TermsDTO;
import com.example.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    private final ModelMapper modelMapper;


    // 예시) 이용 약관 생성
    public TermsDTO.Response create(TermsDTO.Request request) {
        Terms terms = modelMapper.map(request, Terms.class);

        termsRepository.save(terms);
        return modelMapper.map(terms, TermsDTO.Response.class);
    }

    // 예시) 모든 이용 약관들 조회
    public List<TermsDTO.Response> getAll() {
        List<Terms> termsList = termsRepository.findAll();

        return termsList.stream().map(t -> modelMapper.map(t, TermsDTO.Response.class))
                .collect(Collectors.toList());
    }

}
