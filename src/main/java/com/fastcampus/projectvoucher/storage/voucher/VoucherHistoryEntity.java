package com.fastcampus.projectvoucher.storage.voucher;

import com.fastcampus.projectvoucher.common.type.RequesterType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "voucher_history")
public class VoucherHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    @Enumerated(EnumType.STRING)
    private RequesterType requesterType;

    private String requesterId;

    @Enumerated(EnumType.STRING)
    private VoucherStatusType status;

    private String description;

    public VoucherHistoryEntity(String orderId, RequesterType requesterType, String requesterId, VoucherStatusType status, String description) {
        this.orderId = orderId;
        this.requesterType = requesterType;
        this.requesterId = requesterId;
        this.status = status;
        this.description = description;
    }
}

