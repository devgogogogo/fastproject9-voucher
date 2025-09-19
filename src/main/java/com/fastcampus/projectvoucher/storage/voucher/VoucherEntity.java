package com.fastcampus.projectvoucher.storage.voucher;

import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "voucher")
public class VoucherEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private VoucherStatusType status;
    private LocalDate validFrom;  //사용 유효기간
    private LocalDate validTo;

    @Enumerated(EnumType.STRING)
    private VoucherAmountType amount;

    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, VoucherAmountType amount) {
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;
    }

    public void disable() {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용 불가 처리할 수 없는 상태의 상품권 입니다.");
        }
        this.status = VoucherStatusType.DISABLE;
    }

    public void use() {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용할 수 없는 상태의 상품권 입니다.");
        }
        this.status = VoucherStatusType.USE;

    }
}

