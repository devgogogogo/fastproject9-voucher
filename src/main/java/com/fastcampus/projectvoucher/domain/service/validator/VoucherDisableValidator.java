package com.fastcampus.projectvoucher.domain.service.validator;

import com.fastcampus.projectvoucher.common.dto.RequestContext;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherDisableValidator {


    public void validate(VoucherEntity voucherEntity, RequestContext requestContext) {
        //상품권 사용 불가 처리 권한이 있는지 확인
        isValidAuth(voucherEntity, requestContext);
    }

    private static void isValidAuth(VoucherEntity voucherEntity, RequestContext requestContext) {
        if (voucherEntity.publishHistory().getRequesterType() != requestContext.requesterType()
                || !voucherEntity.publishHistory().getRequesterId().equals(requestContext.requesterId())) {
            throw new IllegalArgumentException("사용 불가 처리 권한이 없는 상품권 입니다.");
        }
    }

}
