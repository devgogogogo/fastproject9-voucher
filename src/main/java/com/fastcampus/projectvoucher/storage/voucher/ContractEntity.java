package com.fastcampus.projectvoucher.storage.voucher;

import com.fastcampus.projectvoucher.storage.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "contract")
public class ContractEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; //계약의 고유 코드
    private LocalDate validFrom;  //계약의 유요기간 시작일
    private LocalDate validTo;  //계약의 유요기간 종료일
    private Integer voucherValidPeriodDayCount; //상품권 유효일자

    public ContractEntity(String code, LocalDate validFrom, LocalDate validTo, Integer voucherValidPeriodDayCount) {
        this.code = code;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.voucherValidPeriodDayCount = voucherValidPeriodDayCount;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(validTo);
    }
}

