package com.example.loan.domain;

import com.example.loan.dto.CounselDTO;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;


// 대출 상담
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_deleted=false") // soft delete 방식, false면 삭제 안됐음
public class Counsel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long counselId;

    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '신청일자'")
    private LocalDateTime appliedAt;

    @Column(columnDefinition = "varchar(12) DEFAULT NULL COMMENT '상담 요청자'")
    private String name;

    @Column(columnDefinition = "varchar(13) DEFAULT NULL COMMENT '전화번호'")
    private String cellPhone;

    @Column(columnDefinition = "varchar(50) DEFAULT NULL COMMENT '상담 요청자 이메일'")
    private String email;

    @Column(columnDefinition = "text DEFAULT NULL COMMENT '상담 메모'")
    private String memo;

    @Column(columnDefinition = "varchar(50) DEFAULT NULL COMMENT '주소'")
    private String address;

    @Column(columnDefinition = "varchar(50) DEFAULT NULL COMMENT '주소 상세'")
    private String addressDetail;

    @Column(columnDefinition = "varchar(5) DEFAULT NULL COMMENT '우편번호'")
    private String zipCode;


    public void updateFromRequest(CounselDTO.Request request) {
        this.setName(request.getName());
        this.setCellPhone(request.getCellPhone());
        this.setEmail(request.getEmail());
        this.setMemo(request.getMemo());
        this.setAddress(request.getAddress());
        this.setAddressDetail(request.getAddressDetail());
        this.setZipCode(request.getZipCode());
        this.setUpdatedAt(LocalDateTime.now()); // 현재 시간으로 업데이트
    }

}