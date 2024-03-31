package com.example.loan.service;


import com.example.loan.domain.Application;
import com.example.loan.domain.Entry;
import com.example.loan.dto.BalanceDTO;
import com.example.loan.dto.EntryDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


// 대출 집행
@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final ApplicationRepository applicationRepository;
    private final BalanceService balanceService;
    private final ModelMapper modelMapper;


    // 대출 집행 등록
    @Transactional
    public EntryDTO.Response create(Long applicationId, EntryDTO.Request request) {
        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        entryRepository.save(entry);

        // 대출 잔고 관리
        balanceService.create(applicationId,
                BalanceDTO.CreateRequest.builder()
                        .entryAmount(request.getEntryAmount())
                        .build()
        );

        return modelMapper.map(entry, EntryDTO.Response.class);
    }


    // 대출 집행 조회
    public EntryDTO.Response get(Long applicationId) {
        Entry entry = entryRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });


//        System.out.println("entryRepository.findByApplicationId(applicationId) : " + entryRepository.findByApplicationId(applicationId));;
//
//        Optional<Entry> optionalEntry = entryRepository.findByApplicationId(applicationId);
//        if (optionalEntry.isPresent()) {
//            Entry entry = optionalEntry.get();
//            return modelMapper.map(entry, EntryDTO.Response.class);
//        }
//        return null;

//        Entry entry = entryRepository.findById(applicationId).orElseThrow(() -> {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        });

        return modelMapper.map(entry, EntryDTO.Response.class);
    }



    private boolean isContractedApplication(Long applicationId) {
        Optional<Application> existed = applicationRepository.findById(applicationId);
        if (existed.isEmpty()) {
            return false;
        }

        return existed.get().getContractedAt() != null;
    }
}
