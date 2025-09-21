package com.fastcampus.projectvoucher.domain.service.validator;

import com.fastcampus.projectvoucher.storage.voucher.ContractEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherPublishValidator {


    public void validate(ContractEntity contractEntity ) {
        if (contractEntity.isExpired()) {
            throw new IllegalStateException("유효기간이 지난 계약입니다.");
        }
    }

}
