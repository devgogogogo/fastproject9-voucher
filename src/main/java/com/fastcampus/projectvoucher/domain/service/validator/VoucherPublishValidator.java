package com.fastcampus.projectvoucher.domain.service.validator;

import com.fastcampus.projectvoucher.storage.voucher.ContractEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherPublishValidator {


    public void validate(ContractEntity contractEntity ) {
        //상품권 발행을 위한 계약 유효기간이 만료되었는지 확인
        isValidContract(contractEntity);
    }

    private static void isValidContract(ContractEntity contractEntity) {
        if (contractEntity.isExpired()) {
            throw new IllegalStateException("유효기간이 지난 계약입니다.");
        }
    }

}
