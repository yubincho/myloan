package com.example.loan.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;


// 대출 심사
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_deleted=false")
public class Judgement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long judgementId;

    @Column(columnDefinition = "bigint NOT NULL COMMENT '신청 ID'")
    private Long applicationId;

    @Column(columnDefinition = "varchar(12) DEFAULT NULL COMMENT '심사자'")
    private String name;

    @Column(columnDefinition = "decimal(15,2) DEFAULT NULL COMMENT '승인 금액'")
    private BigDecimal approvalAmount;

    @Column(columnDefinition = "decimal(5,4) DEFAULT NULL COMMENT '승인 금리'")
    private BigDecimal approvalInterestRate;
}
