package com.example.loan.service;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.CounselRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CounselService {

    private final CounselRepository counselRepository;
    private final ModelMapper modelMapper;

    public CounselDTO.Response create(CounselDTO.Request request) {
        Counsel counsel = modelMapper.map(request, Counsel.class); // dto -> entity
        counsel.setAppliedAt(LocalDateTime.now());

        Counsel created = counselRepository.save(counsel);

        return modelMapper.map(created, CounselDTO.Response.class);  // entity -> dto
    }


    public CounselDTO.Response getById(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        return modelMapper.map(counsel, CounselDTO.Response.class);
    }

    public CounselDTO.Response updateById(Long counselId, CounselDTO.Request request) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        counsel.updateFromRequest(request);
        counselRepository.save(counsel);

        return modelMapper.map(counsel, CounselDTO.Response.class);
        // return CounselDTO.toResponse(counsel);
    }


    // soft delete 방식
    public void deleteById(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        counsel.setIsDeleted(true);

        counselRepository.save(counsel);
    }
}
