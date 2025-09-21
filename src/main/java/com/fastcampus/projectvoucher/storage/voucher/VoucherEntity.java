package com.fastcampus.projectvoucher.storage.voucher;

import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    /**
     * EAGER로 하는 이유 : 바우처 조회를 할 때 엔티티가 몇개든 한번에 조회를 하는것임
     * 바우처 하나에 히스토리가 많아봤가 보통3개 정도?(상품권이라 취소 사용 취소 사용.... 이런식으로 반복하지 않는 이상 이력이 많이 없을것으로 예상)
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    private List<VoucherHistoryEntity> histories = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private ContractEntity contractEntity;


    public VoucherEntity(String code, VoucherStatusType status, VoucherAmountType amount, VoucherHistoryEntity voucherHistoryEntity, ContractEntity contractEntity) {
        this.code = code;
        this.status = status;
        this.validFrom = LocalDate.now();
        this.validTo = LocalDate.now().plusDays(contractEntity.getVoucherValidPeriodDayCount());
        this.amount = amount;
        this.histories.add(voucherHistoryEntity);
        this.contractEntity = contractEntity;
    }

    public void disable(VoucherHistoryEntity voucherHistoryEntity) {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용 불가 처리할 수 없는 상태의 상품권 입니다.");
        }
        this.status = VoucherStatusType.DISABLE;
        this.histories.add(voucherHistoryEntity);
    }

    public void use(VoucherHistoryEntity voucherHistoryEntity) {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용할 수 없는 상태의 상품권 입니다.");
        }
        this.status = VoucherStatusType.USE;
        this.histories.add(voucherHistoryEntity);
    }

    public VoucherHistoryEntity publishHistory() {
        return histories.stream()
                .filter(voucherHistoryEntity -> voucherHistoryEntity.getStatus().equals(VoucherStatusType.PUBLISH))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("사용 불가 처리할 수 없는 상태의 상품권 입니다."));
    }
}

