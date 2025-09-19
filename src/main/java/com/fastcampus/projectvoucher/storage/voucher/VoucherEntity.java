package com.fastcampus.projectvoucher.storage.voucher;

import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "voucher")
public class VoucherEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private VoucherStatusType status;
    private LocalDateTime validFrom;  //사용 유효기간
    private LocalDateTime validTo;
    private String value;
    private Long amount;

    public VoucherEntity(String code, VoucherStatusType status, LocalDateTime validFrom, LocalDateTime validTo, String value, Long amount) {
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.value = value;
        this.amount = amount;
    }
}

