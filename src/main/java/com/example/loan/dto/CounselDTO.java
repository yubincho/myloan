package com.example.loan.dto;

import com.example.loan.domain.Counsel;
import lombok.*;

import java.time.LocalDateTime;

public class CounselDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class Request {
        private String name;
        private String cellPhone;
        private String email;
        private String memo;
        private String address;
        private String addressDetail;
        private String zipCode;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {
        private Long counselId;
        private String name;
        private String cellPhone;
        private String email;
        private String memo;
        private String address;
        private String addressDetail;
        private String zipCode;
        private LocalDateTime appliedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }


//    public static Response toResponse(Counsel counsel) {
//        return Response.builder()
//                .counselId(counsel.getCounselId())
//                .name(counsel.getName())
//                .cellPhone(counsel.getCellPhone())
//                .email(counsel.getEmail())
//                .memo(counsel.getMemo())
//                .address(counsel.getAddress())
//                .addressDetail(counsel.getAddressDetail())
//                .zipCode(counsel.getZipCode())
//                .appliedAt(counsel.getAppliedAt()) // 가정: Counsel 엔티티에 이 필드가 있다고 가정
//                .createdAt(counsel.getCreatedAt())
//                .updatedAt(counsel.getUpdatedAt())
//                .build();
//    }

}


